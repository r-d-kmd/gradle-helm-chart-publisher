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
		return exec("helm version --client --short").split("v")[1]
	}

	def initClient() {
		exec("helm init --client-only")
	}

	def updateDependencies() {
		exec("helm dependency update $chartProperties.chartDefinitionPath")
	}

	def packageChart() {
		def packagedChartDestination = "${workDirProperties.helmChartRepositoryPath}/${chartProperties.chartName}"
		Files.createDirectories(Paths.get(packagedChartDestination))
		exec("helm package " +
				 "--version ${chartProperties.chartVersion} " +
				 "--destination ${packagedChartDestination} " +
				 "${chartProperties.chartDefinitionPath}")
	}

	def reindexRepository(){
		exec("helm repo index ${workDirProperties.helmChartRepositoryPath}")
	}

	def exec(String command) {
		logger.info("executing command: '${command}'")
		def sout = new StringBuilder(), serr = new StringBuilder()
		def proc = command.execute()
		proc.consumeProcessOutput(sout, serr)
		proc.waitFor()

		if (proc.exitValue() == 0) {
			logger.info("result: $sout")
			return sout.toString()
		} else {
			logger.error("Command threw exception: $command")
			throw new Exception(serr.toString())
		}
	}
}
