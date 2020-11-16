package dk.kmd.helm.chart.publish.properties

import dk.kmd.helm.chart.publish.HelmChartPublisherExtension
import org.gradle.api.Project

class WorkDirPropertiesProvider {

	static String GIT_CHART_REPO_WORK_DIR_PROPERTY = "gitChartRepo.workDir"


	Project project
	HelmChartPublisherExtension extension

	WorkDirPropertiesProvider(Project project, HelmChartPublisherExtension extension) {
		this.project = project
		this.extension = extension
	}

	WorkDirProperties provide() {
		def clonedGitChartRepositoryPath = "${project.buildDir}/helm-chart-repository"
		return new WorkDirProperties(clonedGitChartRepositoryPath: clonedGitChartRepositoryPath, helmChartRepositoryPath: "${clonedGitChartRepositoryPath}/${chartRepositoryWorkDir()}")
	}


	private def chartRepositoryWorkDir() {
		if (project.hasProperty(GIT_CHART_REPO_WORK_DIR_PROPERTY)) {
			return project.property(GIT_CHART_REPO_WORK_DIR_PROPERTY)
		}
		if (extension.gitChartRepoWorkDir) {
			return extension.gitChartRepoWorkDir
		}
		return ""
	}
}
