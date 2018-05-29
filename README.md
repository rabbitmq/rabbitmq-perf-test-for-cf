# RabbitMQ PerfTest for Pivotal Cloud Foundry

This is a template project to easily run [RabbitMQ Performance Tool](http://www.rabbitmq.com/java-tools.html#throughput-load-testing)
on [Pivotal Cloud Foundry](https://pivotal.io/platform).

## How to use

* Compile and package the application: `./mvnw clean package`
* Configure the application with the `manifest.yml` file. PerfTest command line arguments
are specified with `JBP_CONFIG_JAVA_MAIN` key.
* Push the application to Cloud Foundry: `cf push`.

## Community / Support

* [GitHub Issues](https://github.com/rabbitmq/rabbitmq-perf-test-pcf/issues)

## License ##

RabbitMQ PerfTest for Pivotal Cloud Foundry is [Apache 2.0 licensed](http://www.apache.org/licenses/LICENSE-2.0.html).

_Sponsored by [Pivotal](http://pivotal.io)_
