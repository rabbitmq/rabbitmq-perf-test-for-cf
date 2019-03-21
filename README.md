# RabbitMQ PerfTest for Cloud Foundry

This is a template project to easily run [RabbitMQ Performance Tool](http://www.rabbitmq.com/java-tools.html#throughput-load-testing)
on [Cloud Foundry](https://www.cloudfoundry.org/). It has been tested against
[Pivotal Cloud Foundry](https://pivotal.io/platform), more specifically against
[Pivotal Cloud Foundry for Local Development](https://pivotal.io/pcf-dev).

## How to use

Pre-requisites: JDK 8 or higher and
[Cloud Foundry Command Line Interface](https://docs.cloudfoundry.org/cf-cli/) installed.

* Compile and package the application: `./mvnw clean package -Dmaven.test.skip`
* Configure the application with the `manifest.yml` file. PerfTest is configured
with environment variables or with the `JBP_CONFIG_JAVA_MAIN` manifest key.
* Push the application to Cloud Foundry: `cf push`.

The Cloud Foundry application must be bound to a RabbitMQ service. It will automatically
get the connection information from the `VCAP_SERVICES` environment variable.

Consult [PerfTest documentation](http://www.rabbitmq.com/java-tools.html#throughput-load-testing)
to learn more about the available options.

You can use environment variables in the manifest to configure PerfTest.
The environment variable names use a snake-case version of the command line
argument long names, e.g. `--multi-ack-every` becomes `MULTI_ACK_EVERY`. Run the following
command to see all the available environment variables:

```
./mvnw -q compile exec:java -Dexec.args="-env"
```

Alternatively, you can use the `JBP_CONFIG_JAVA_MAIN` manifest key to specify
the command line for PerfTest. Run the following command to see
all the available command line arguments:

```
./mvnw -q compile exec:java -Dexec.args="--help"
```

Below are some simple examples showing configuration with both ways.

PerfTest runs 1 producer and 1 consumer by default, the following snippet
shows how to use 1 producer and 2 consumers:
```yaml
# with environment variables
env:
  PRODUCERS: 1
  CONSUMERS: 2
# or with the full command line
env:
  JBP_CONFIG_JAVA_MAIN: >
    { arguments: "-x 1 -y 2" }
```

PerfTest uses by default 12-byte long messages, here is how to use 1 kB messages:
```yaml
# with environment variables
env:
  PRODUCERS: 1
  CONSUMERS: 2
  SIZE: 1000
# or with the full command line
env:
  JBP_CONFIG_JAVA_MAIN: >
    { arguments: "-x 1 -y 2 -s 1000" }
```

Producers publish as fast as possible by default, here is how to limit
the publishing rate to 500 messages / second:
```yaml
# with environment variables
env:
  PRODUCERS: 1
  CONSUMERS: 2
  RATE: 500
# or with the full command line
env:
  JBP_CONFIG_JAVA_MAIN: >
    { arguments: "-x 1 -y 2 --rate 500" }
```

PerfTest uses by default one queue, here is how to define a sequence of queues,
from `test-1` to `test-10`:
```yaml
# with environment variables
env:
  PRODUCERS: 10
  CONSUMERS: 20
  QUEUE_PATTERN: test-%d
  QUEUE_PATTERN_FROM: 1
  QUEUE_PATTERN_TO: 10
# or with the full command line
env:
  JBP_CONFIG_JAVA_MAIN: >
    { arguments: "-x 10 -y 20 --queue-pattern 'test-%d' --queue-pattern-from 1 --queue-pattern-to 10" }
```

See PerfTest documentation on [high load simulation](http://www.rabbitmq.com/java-tools.html#simulating-high-loads)
if you want to run hundreds of connections or more.

## Metrics Support

PerfTest has also options for metrics. Run the following command to see the
available options as environment variables:

```
./mvnw -q compile exec:java -Dexec.args="-mh -env"
```

Launch the following command to see the available options as command line arguments:

```
./mvnw -q compile exec:java -Dexec.args="-mh"
```

## Community / Support

* [GitHub Issues](https://github.com/rabbitmq/rabbitmq-perf-test-for-cf/issues)

## License ##

RabbitMQ PerfTest for Pivotal Cloud Foundry is [Apache 2.0 licensed](https://www.apache.org/licenses/LICENSE-2.0.html).

_Sponsored by [Pivotal](http://pivotal.io)_
