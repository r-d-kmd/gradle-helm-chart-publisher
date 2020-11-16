package dk.kmd.helm.chart.publish

class HelmChartPublisherExtension {

	static final String NAME = "helmChartPublisher"

	int retries = 5

	String gitUsername
	String gitChartRepoUrl
	String gitChartRepoWorkDir

	String chartName
	String chartDefinitionPath
}
