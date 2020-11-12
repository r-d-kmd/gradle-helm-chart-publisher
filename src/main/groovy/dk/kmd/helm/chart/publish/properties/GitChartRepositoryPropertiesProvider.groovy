package dk.kmd.helm.chart.publish.properties

import dk.kmd.helm.chart.publish.HelmChartPublisherExtension
import org.gradle.api.Project

class GitChartRepositoryPropertiesProvider {

	static String GIT_CHART_REPO_URL_PROPERTY = "gitChartRepo.url"
	static String GIT_CHART_REPO_USERNAME_PROPERTY = "gitChartRepo.username"
	static String GIT_CHART_REPO_PASSWORD_PROPERTY = "gitChartRepo.password"
	static String GIT_CHART_REPO_WORK_DIR_PROPERTY = "gitChartRepo.workDir"
	static String GIT_CHART_REPO_USERNAME_ENV_VARIABLE = "HELM_CHART_PUBLISH_GIT_REPO_USERNAME"
	static String GIT_CHART_REPO_PASSWORD_ENV_VARIABLE = "HELM_CHART_PUBLISH_GIT_REPO_PASSWORD"

	private Project project
	private HelmChartPublisherExtension extension

	GitChartRepositoryPropertiesProvider(Project project, HelmChartPublisherExtension extension) {
		this.project = project
		this.extension = extension
	}

	GitChartRepositoryProperties provide() {
		return new GitChartRepositoryProperties(url: gitChartRepoUrl(),
												username: gitChartRepoUsername(),
												password: gitChartRepoPassword())
	}

	private def gitChartRepoUrl() {
		if (project.hasProperty(GIT_CHART_REPO_URL_PROPERTY)) {
			return project.property(GIT_CHART_REPO_URL_PROPERTY)
		} else if (extension.gitChartRepoUrl) {
			return extension.gitChartRepoUrl
		}
		throw new IllegalStateException(
			"""gitChartRepo.url property is not set. You can set it in build.gradle in helmChartPublisher section like so
 			helmChartPublisher {
 				gitChartRepoUrl = "https://your.repo.url"
 			}
 			or as a command line parameter gitChartRepo.url""")
	}

	private def gitChartRepoUsername() {
		if (project.hasProperty(GIT_CHART_REPO_USERNAME_PROPERTY)) {
			return project.property(GIT_CHART_REPO_USERNAME_PROPERTY)
		}
		if (System.getenv().containsKey(GIT_CHART_REPO_USERNAME_ENV_VARIABLE)) {
			return System.getenv(GIT_CHART_REPO_USERNAME_ENV_VARIABLE)
		}
		return extension.gitUsername
	}

	private def gitChartRepoPassword() {
		if (project.hasProperty(GIT_CHART_REPO_PASSWORD_PROPERTY)) {
			return project.property(GIT_CHART_REPO_PASSWORD_PROPERTY)
		}
		if (System.getenv().containsKey(GIT_CHART_REPO_PASSWORD_ENV_VARIABLE)) {
			return System.getenv(GIT_CHART_REPO_PASSWORD_ENV_VARIABLE)
		}
		return null
	}
}
