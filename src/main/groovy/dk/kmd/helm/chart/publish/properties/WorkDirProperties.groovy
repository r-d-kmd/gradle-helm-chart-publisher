package dk.kmd.helm.chart.publish.properties

class WorkDirProperties {

	private String chartRepoTmpDirectory
	private String chartRepoWorkingDirectory

	String getChartRepoTmpDirectory() {
		return chartRepoTmpDirectory
	}

	String getChartRepoWorkingDirectory() {
		return chartRepoWorkingDirectory
	}
}
