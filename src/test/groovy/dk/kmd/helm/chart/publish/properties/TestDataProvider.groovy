package dk.kmd.helm.chart.publish.properties

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric

import dk.kmd.helm.chart.publish.HelmChartPublisherExtension

class TestDataProvider {

	static HelmChartPublisherExtension emptyExtension() {
		return new HelmChartPublisherExtension()
	}

	static HelmChartPublisherExtension extensionWithAnyParams(Map params = [:]) {
		def defaults = [retries            : 5,
						gitChartRepoUrl    : randomUrl(),
						gitUsername        : randomName(),
						gitChartRepoWorkDir: randomAlphabetic(4),
						chartName          : randomName(),
						chartDefinitionDir : randomAlphabetic(8)]
		return new HelmChartPublisherExtension(defaults << params)
	}

	static randomName() {
		return randomAlphabetic(8).toLowerCase()
	}

	static randomVersion() {
		return "${randomNumeric(1)}.${randomNumeric(1)}.${randomNumeric(1)}"
	}

	static randomDir() {
		return "${randomAlphabetic(5)}/${randomAlphabetic(6)}/${randomAlphabetic(4)}"
	}

	static randomUrl() {
		return "https://${randomAlphabetic(5)}.${randomAlphabetic(3)}/${randomAlphabetic(4)}"
	}
}
