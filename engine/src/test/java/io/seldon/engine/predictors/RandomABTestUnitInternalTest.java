package io.seldon.engine.predictors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.seldon.engine.exception.APIException;
import io.seldon.protos.PredictionProtos.PredictionRequestDef;

public class RandomABTestUnitInternalTest {

	@Test
	public void simpleCase() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		PredictionRequestDef request = PredictionRequestDef.newBuilder().build();
		
		PredictiveUnitParameter<Float> ratioParam = new PredictiveUnitParameter<Float>(0.5F);
    	Map<String,PredictiveUnitParameterInterface> params = new HashMap<>();
    	params.put("ratioA", ratioParam);
		
		PredictiveUnitState state = new PredictiveUnitState("1","Cool_name",null,params);
		
		PredictiveUnitState childA = new PredictiveUnitState("2","A",null,null);
		PredictiveUnitState childB = new PredictiveUnitState("3","B",null,null);
		
		state.addChild("0", childA);
		state.addChild("1", childB);
//		
//		Map<String,Integer> emptyDict = new HashMap<String, Integer>();
//		Class<Map<String,Integer>> clazz = (Class<Map<String,Integer>>)(Class)Map.class;

		
		RandomABTestUnit randomABTestUnit = new RandomABTestUnit();
		Method method = RandomABTestUnit.class.getDeclaredMethod("forwardPass", PredictionRequestDef.class, PredictiveUnitState.class);
		method.setAccessible(true);

		// The following values are from random seed 1337
		Integer routing1 = (Integer) method.invoke(randomABTestUnit, request, state);

		Assert.assertEquals((Integer)1,routing1); 
		
		Integer routing2 = (Integer) method.invoke(randomABTestUnit, request, state);

		Assert.assertEquals((Integer)0,routing2);
		
		Integer routing3 = (Integer) method.invoke(randomABTestUnit, request, state);

		Assert.assertEquals((Integer)1,routing3);
	}
	
	@Test(expected=APIException.class)
	public void failureOneChild() throws Throwable{
		
		PredictionRequestDef request = PredictionRequestDef.newBuilder().build();
		
		PredictiveUnitParameter<Float> ratioParam = new PredictiveUnitParameter<Float>(0.5F);
    	Map<String,PredictiveUnitParameterInterface> params = new HashMap<>();
    	params.put("ratioA", ratioParam);
		
		PredictiveUnitState state = new PredictiveUnitState("1","Cool_name",null,params);
		
		PredictiveUnitState childA = new PredictiveUnitState("2","A",null,null);
		
		state.addChild("0", childA);
		
//		Map<String,Integer> emptyDict = new HashMap<String, Integer>();
		
		RandomABTestUnit randomABTestUnit = new RandomABTestUnit();
		Method method = RandomABTestUnit.class.getDeclaredMethod("forwardPass", PredictionRequestDef.class, PredictiveUnitState.class);
		method.setAccessible(true);

		// The following should return an error
		try{
			Integer routing1 = (Integer) method.invoke(randomABTestUnit, request, state);
		}
		catch( InvocationTargetException e){
			Throwable targetException = e.getTargetException();
			throw targetException;
		}
	}
}
