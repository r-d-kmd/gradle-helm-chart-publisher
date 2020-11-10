package dk.kmd.helm.chart.publish.properties

import org.gradle.api.Project

class WorkDirPropertiesProvider {

	Project project

	WorkDirPropertiesProvider(Project project) {
		this.project = project
	}

	WorkDirProperties provide() {
		return new WorkDirProperties(chartRepoTmpDirectory: "${project.buildDir}/helm-chart-repository")
	}

}
