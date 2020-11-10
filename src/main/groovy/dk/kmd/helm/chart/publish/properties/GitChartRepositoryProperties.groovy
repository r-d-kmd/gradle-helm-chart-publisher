package dk.kmd.helm.chart.publish.properties

class GitChartRepositoryProperties {

	private String url
	private String username
	private String password
	private String chartRepoWorkDir

	String getUrl() {
		return url
	}

	String getUsername() {
		return username
	}

	String getPassword() {
		return password
	}

	String getChartRepoWorkDir() {
		return chartRepoWorkDir
	}
}
