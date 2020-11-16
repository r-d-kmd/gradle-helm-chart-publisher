package dk.kmd.helm.chart.publish.properties

class WorkDirProperties {

	private String clonedGitChartRepositoryPath
	private String helmChartRepositoryPath

	String getClonedGitChartRepositoryPath() {
		return clonedGitChartRepositoryPath
	}

	String getHelmChartRepositoryPath() {
		return helmChartRepositoryPath
	}
}
