package dk.kmd.helm.chart.publish.infrastructure

import dk.kmd.helm.chart.publish.properties.ChartProperties
import dk.kmd.helm.chart.publish.properties.GitChartRepositoryProperties
import dk.kmd.helm.chart.publish.properties.WorkDirProperties
import org.ajoberstar.grgit.Credentials
import org.ajoberstar.grgit.Grgit
import org.gradle.api.logging.Logger

class GitChartRepository {

	GitChartRepositoryProperties gitChartRepositoryProperties
	WorkDirProperties workDirProperties
	ChartProperties chartProperties
	Logger logger

	GitChartRepository(GitChartRepositoryProperties gitChartRepositoryProperties, WorkDirProperties workDirProperties, ChartProperties chartProperties, Logger logger) {
		this.gitChartRepositoryProperties = gitChartRepositoryProperties
		this.workDirProperties = workDirProperties
		this.chartProperties = chartProperties
		this.logger = logger
	}

	def cloneRepository() {
		deleteDir(workDirProperties.clonedGitChartRepositoryPath)
		Grgit.clone(dir: workDirProperties.clonedGitChartRepositoryPath, uri: gitChartRepositoryProperties.url, credentials: new Credentials(gitChartRepositoryProperties.username, gitChartRepositoryProperties.password))
	}

	def pushToRepository() {
		def grgit = Grgit.open(dir: workDirProperties.clonedGitChartRepositoryPath, credentials: new Credentials(gitChartRepositoryProperties.username, gitChartRepositoryProperties.password))
		grgit.add(patterns: [".", "index.yaml"])
		logger.info("Committing files: ${grgit.status().staged.allChanges}")
		grgit.commit(message: "Publishing ${chartProperties.chartName} chart in version ${chartProperties.chartVersion}", all: true)
		grgit.push()
	}

	def deleteDir(String dir) {
		new File(dir).deleteDir()
	}

}
