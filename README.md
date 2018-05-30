# RabbitMQ PerfTest for Cloud Foundry

This is a template project to easily run [RabbitMQ Performance Tool](http://www.rabbitmq.com/java-tools.html#throughput-load-testing)
on [Cloud Foundry](https://www.cloudfoundry.org/). It has been tested against
[Pivotal Cloud Foundry](https://pivotal.io/platform), more specifically against
[Pivotal Cloud Foundry for Local Development](https://pivotal.io/pcf-dev).

## How to use

* Compile and package the application: `./mvnw clean package`
* Configure the application with the `manifest.yml` file. PerfTest command line arguments
are specified with the `JBP_CONFIG_JAVA_MAIN` key.
* Push the application to Cloud Foundry: `cf push`.

Here are a few examples of commands, consult [PerfTest documentation](http://www.rabbitmq.com/java-tools.html#throughput-load-testing)
to learn more about the available options.

PerfTest runs 1 producer and 1 consumer by default, the following snippet
shows how to use 1 producer and 2 consumers:
```yaml
JBP_CONFIG_JAVA_MAIN: >
  { arguments: "-x 1 -y 2" }
```

PerfTest uses by default 12-byte long messages, here is how to use 1 kB messages:
```yaml
JBP_CONFIG_JAVA_MAIN: >
  { arguments: "-x 1 -y 2 -s 1000" }
```

Producers publish as fast as possible, here is how to limit
the publishing rate to 500 messages / second:
```yaml
JBP_CONFIG_JAVA_MAIN: >
  { arguments: "-x 1 -y 2 --rate 500" }
```

PerfTest uses by default one queue, here is how to define a sequence of queues,
from `test-1` to `test-10`:
```yaml
JBP_CONFIG_JAVA_MAIN: >
  { arguments: "-x 10 -y 20 --queue-pattern 'test-%d' --queue-pattern-from 1 --queue-pattern-to 10" }
```

See PerfTest documentation on [high load simulation](http://www.rabbitmq.com/java-tools.html#simulating-high-loads)
if you want to run hundreds of connections or more.

## Community / Support

* [GitHub Issues](https://github.com/rabbitmq/rabbitmq-perf-test-for-cf/issues)

## License ##

RabbitMQ PerfTest for Pivotal Cloud Foundry is [Apache 2.0 licensed](http://www.apache.org/licenses/LICENSE-2.0.html).

_Sponsored by [Pivotal](http://pivotal.io)_
