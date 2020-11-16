package dk.kmd.helm.chart.publish.properties

import dk.kmd.helm.chart.publish.HelmChartPublisherExtension
import org.gradle.api.Project

class ChartPropertiesProvider {

	static String CHART_VERSION_PROPERTY = "chart.version"
	static String CHART_NAME_PROPERTY = "chart.name"
	static String CHART_DEFINITION_PATH_PROPERTY = "chart.definitionPath"

	private Project project
	private HelmChartPublisherExtension extension

	ChartPropertiesProvider(Project project, HelmChartPublisherExtension extension) {
		this.project = project
		this.extension = extension
	}

	ChartProperties provide() {
		return new ChartProperties(chartVersion: getChartVersion(), chartName: getChartName(), chartDefinitionPath: getChartDefinitionDir())
	}

	def getChartVersion() {
		if (project.hasProperty(CHART_VERSION_PROPERTY)) {
			return project.property(CHART_VERSION_PROPERTY)
		}
		return project.version
	}

	def getChartName() {
		if (project.hasProperty(CHART_NAME_PROPERTY)) {
			return project.property(CHART_NAME_PROPERTY)
		}
		if (extension.chartName != null) {
			return extension.chartName
		}
		return project.name
	}

	def getChartDefinitionDir() {
		if (project.hasProperty(CHART_DEFINITION_PATH_PROPERTY)) {
			return project.property(CHART_DEFINITION_PATH_PROPERTY)
		}
		if (extension.chartDefinitionPath != null) {
			return extension.chartDefinitionPath
		}
		return "${project.projectDir}/helm/${getChartName()}"
	}
}
