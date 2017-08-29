package io.seldon.engine.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.seldon.engine.predictors.EnginePredictor;
import io.seldon.engine.predictors.PredictorBean;
import io.seldon.engine.predictors.PredictorState;
import io.seldon.protos.PredictionProtos.PredictionFeedbackDef;
import io.seldon.protos.PredictionProtos.PredictionRequestDef;
import io.seldon.protos.PredictionProtos.PredictionRequestMetaDef;
import io.seldon.protos.PredictionProtos.PredictionResponseDef;
import io.seldon.protos.PredictionProtos.PredictionResponseMetaDef;

@Service
public class PredictionService {
	
	private static Logger logger = LoggerFactory.getLogger(PredictionService.class.getName());
	
	private final ExecutorService pool = Executors.newFixedThreadPool(50);
	
//	@Autowired
//	PredictorsStore predictorsStore;
	
	@Autowired
	PredictorBean predictorBean;
	
	@Autowired
	EnginePredictor enginePredictor;
	
	PuidGenerator puidGenerator = new PuidGenerator();

	public final class PuidGenerator {
	    private SecureRandom random = new SecureRandom();

	    public String nextPuidId() {
	        return new BigInteger(130, random).toString(32);
	    }
	}
	
	public void sendFeedback(PredictionFeedbackDef feedback) throws InterruptedException, ExecutionException
	{
		PredictorState predictorState = predictorBean.predictorStateFromDeploymentDef(enginePredictor.getPredictorDef());

		predictorBean.sendFeedback(feedback, predictorState);
		
		return;
	}
	
	public PredictionResponseDef predict(PredictionRequestDef request) throws InterruptedException, ExecutionException
	{

		if (!request.hasMeta())
		{
			request = request.toBuilder().setMeta(PredictionRequestMetaDef.newBuilder().setPuid(puidGenerator.nextPuidId()).build()).build();
		}
		else if (StringUtils.isEmpty(request.getMeta().getPuid()))
		{
			request = request.toBuilder().setMeta(request.getMeta().toBuilder().setPuid(puidGenerator.nextPuidId()).build()).build();
		}
		String puid = request.getMeta().getPuid();
		
        PredictorState predictorState = predictorBean.predictorStateFromDeploymentDef(enginePredictor.getPredictorDef());

        PredictionResponseDef predictorReturn = predictorBean.predict(request,predictorState);
			
        PredictionResponseDef.Builder builder = PredictionResponseDef.newBuilder(predictorReturn).setMeta(PredictionResponseMetaDef.newBuilder(predictorReturn.getMeta()).setPuid(puid));

        return builder.build();
		
	}
}
