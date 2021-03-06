# seldon-mab

![Version: 0.2.0](https://img.shields.io/badge/Version-0.2.0-informational?style=flat-square)

Chart to deploy a multi-armed bandits router over two Seldon deployments, so
that traffic is sent to the best performing model.
You will need to utilize both the `predict` and `send_feedback` API methods.

## Usage

To use this chart, you will first need to add the `seldonio` Helm repo:

```shell
helm repo add seldonio https://storage.googleapis.com/seldon-charts
helm repo update
```

Once that's done, you should then be able to use the inference graph template as:

```shell
helm template $MY_MODEL_NAME seldonio/seldon-mab --namespace $MODELS_NAMESPACE
```

Note that you can also deploy the inference graph directly to your cluster
using:

```shell
helm install $MY_MODEL_NAME seldonio/seldon-mab --namespace $MODELS_NAMESPACE
```

**Homepage:** <https://github.com/SeldonIO/seldon-core>

## Source Code

* <https://github.com/SeldonIO/seldon-core>
* <https://github.com/SeldonIO/seldon-core/tree/master/helm-charts/seldon-mab>

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| engine.env.SELDON_LOG_MESSAGES_EXTERNALLY | bool | `false` |  |
| engine.env.SELDON_LOG_MESSAGE_TYPE | string | `"seldon.message.pair"` |  |
| engine.env.SELDON_LOG_REQUESTS | bool | `false` |  |
| engine.env.SELDON_LOG_RESPONSES | bool | `false` |  |
| engine.resources.requests.cpu | string | `"0.1"` |  |
| mab.branches | int | `2` |  |
| mab.epsilon | float | `0.2` |  |
| mab.image.name | string | `"seldonio/mab_epsilon_greedy"` |  |
| mab.image.version | float | `1.3` |  |
| mab.name | string | `"eg-router"` |  |
| mab.verbose | int | `1` |  |
| modela.endpoint | string | `"REST"` |  |
| modela.image.name | string | `"seldonio/mock_classifier"` |  |
| modela.image.version | float | `1.3` |  |
| modela.name | string | `"classifier-1"` |  |
| modelb.endpoint | string | `"REST"` |  |
| modelb.image.name | string | `"seldonio/mock_classifier"` |  |
| modelb.image.version | float | `1.3` |  |
| modelb.name | string | `"classifier-2"` |  |
| oauth.key | string | `nil` |  |
| oauth.secret | string | `nil` |  |
| predictor.name | string | `"default"` |  |
| predictorLabels.fluentd | string | `"true"` |  |
| predictorLabels.version | string | `"v1"` |  |
| protocol | string | `"REST"` |  |
| replicas | int | `1` |  |
| sdepLabels.app | string | `"seldon"` |  |
