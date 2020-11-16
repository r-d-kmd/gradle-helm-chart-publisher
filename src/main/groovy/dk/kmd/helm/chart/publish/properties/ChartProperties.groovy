package dk.kmd.helm.chart.publish.properties

class ChartProperties {

	private String chartName
	private String chartVersion
	private String chartDefinitionPath

	String getChartName() {
		return chartName
	}

	String getChartVersion() {
		return chartVersion
	}

	String getChartDefinitionPath() {
		return chartDefinitionPath
	}
}
