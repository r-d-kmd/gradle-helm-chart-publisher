package dk.kmd.helm.chart.publish


import org.gradle.api.Plugin
import org.gradle.api.Project

class HelmChartPublisherPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.extensions.create(HelmChartPublisherExtension.NAME, HelmChartPublisherExtension)
		project.tasks.create("releaseHelmChart", ReleaseHelmChartTask)
	}

}
