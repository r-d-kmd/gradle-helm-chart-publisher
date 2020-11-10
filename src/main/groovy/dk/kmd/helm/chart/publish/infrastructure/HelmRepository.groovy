package dk.kmd.helm.chart.publish.infrastructure

import dk.kmd.helm.chart.publish.properties.ChartProperties
import dk.kmd.helm.chart.publish.properties.WorkDirProperties
import java.nio.file.Files
import java.nio.file.Paths
import org.gradle.api.logging.Logger

class HelmRepository {

	ChartProperties chartProperties
	WorkDirProperties workDirProperties
	Logger logger

	HelmRepository(ChartProperties chartProperties, WorkDirProperties workDirProperties, Logger logger) {
		this.chartProperties = chartProperties
		this.workDirProperties = workDirProperties
		this.logger = logger
	}

	def version() {
		exec("helm version --client")
	}

	def initClient() {
		exec("helm init --client-only")
	}

	def packageChart() {
		def packagedChartDestination = "${workDirProperties.chartRepoTmpDirectory}/${chartProperties.chartName}"
		Files.createDirectories(Paths.get(packagedChartDestination));
		exec("helm package " +
				 "--version ${chartProperties.chartVersion} " +
				 "--destination ${packagedChartDestination} " +
				 "${chartProperties.chartDefinitionDir}")
	}

	def reindexRepository(){
		exec("helm repo index ${workDirProperties.chartRepoTmpDirectory}")
	}

	def exec(String command) {
		logger.info("executing command: '${command}'")
		def sout = new StringBuilder(), serr = new StringBuilder()
		def proc = command.execute()
		proc.consumeProcessOutput(sout, serr)
		proc.waitFor()

		if (proc.exitValue() == 0) {
			logger.info("result: $sout")
		} else {
			logger.error("Command threw exception: $command")
			throw new Exception(serr.toString())
		}
		return proc
	}
}
