package dk.kmd.helm.chart.publish

import dk.kmd.helm.chart.publish.infrastructure.GitChartRepository
import dk.kmd.helm.chart.publish.infrastructure.HelmRepository
import dk.kmd.helm.chart.publish.infrastructure.Retryable
import dk.kmd.helm.chart.publish.properties.ChartPropertiesProvider
import dk.kmd.helm.chart.publish.properties.GitChartRepositoryPropertiesProvider
import dk.kmd.helm.chart.publish.properties.WorkDirPropertiesProvider
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReleaseHelmChartTask extends DefaultTask {

	@TaskAction
	def publishChart() {
		def extension = project.extensions.getByType(HelmChartPublisherExtension)

		def gitChartRepositoryProperties = new GitChartRepositoryPropertiesProvider(project, extension).provide()
		def chartProperties = new ChartPropertiesProvider(project, extension).provide()
		def workDirProperties = new WorkDirPropertiesProvider(project).provide()

		def helmClient = new HelmRepository(chartProperties, workDirProperties, project.logger)
		def gitChartRepository = new GitChartRepository(gitChartRepositoryProperties, workDirProperties, chartProperties, project.logger)

		if (helmClient.version().startsWith("2")) {
			helmClient.initClient()
		}
		new Retryable(extension.retries, logger).execute {
			gitChartRepository.cloneRepository()
			helmClient.packageChart()
			helmClient.reindexRepository()
			gitChartRepository.pushToRepository()
		}
	}

}