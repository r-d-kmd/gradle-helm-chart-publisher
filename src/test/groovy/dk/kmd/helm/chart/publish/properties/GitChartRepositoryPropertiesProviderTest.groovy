package dk.kmd.helm.chart.publish.properties

import static dk.kmd.helm.chart.publish.properties.TestDataProvider.emptyExtension
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.extensionWithAnyParams
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomName
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomUrl

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables

class GitChartRepositoryPropertiesProviderTest extends Specification {

	private EnvironmentVariables environmentVariables = new EnvironmentVariables()

	def "should resolve git repository url from property if set"() {
		given:
		def givenUrl = randomUrl()
		Project project = ProjectBuilder.builder().build()
		project.extensions.extraProperties.set("gitChartRepo.url", givenUrl)

		expect:
		new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().url == givenUrl
	}

	def "should use extension if gitChartRepo.url property not set"() {
		given:
		def givenUrl = randomUrl()
		Project project = ProjectBuilder.builder().build()

		def extension = extensionWithAnyParams(gitChartRepoUrl: givenUrl)
		expect:
		new GitChartRepositoryPropertiesProvider(project, extension).provide().url == givenUrl
	}

	def "should throw exception if gitChartRepo.url not set"() {
		when:
		new GitChartRepositoryPropertiesProvider(ProjectBuilder.builder().build(), emptyExtension()).provide()

		then:
		thrown(Exception)
	}

	def "should resolve git username from property if set"() {
		given:
		def givenUsername = randomName()
		def resultUsername = ""
		environmentVariables.set("HELM_CHART_PUBLISH_GIT_REPO_USERNAME", randomName()).execute(() -> {
			Project project = ProjectBuilder.builder().build()
			project.extensions.extraProperties.set("gitChartRepo.username", givenUsername)
			resultUsername = new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().username
		})

		expect:
		resultUsername == givenUsername
	}

	def "should resolve git username from env variable if set"() {
		given:
		def givenUsername = "user-from-env"
		def resultUsername = ""
		environmentVariables.set("HELM_CHART_PUBLISH_GIT_REPO_USERNAME", givenUsername).execute(() -> {
			Project project = ProjectBuilder.builder().build()
			resultUsername = new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().username
		} )

		expect:
		resultUsername == givenUsername
	}

	def "should resolve git username from extension if not set"() {
		given:
		def givenUsername = randomName()
		Project project = ProjectBuilder.builder().build()
		def extension = extensionWithAnyParams(gitUsername: givenUsername)

		expect:
		new GitChartRepositoryPropertiesProvider(project, extension).provide().username == givenUsername
	}

	def "should resolve git password from property if set"() {
		given:
		def givenPassword = "pass-from-property"
		def resultPassword = ""
		environmentVariables.set("HELM_CHART_PUBLISH_GIT_REPO_PASSWORD", "pass-from-envs1").execute(() -> {
			Project project = ProjectBuilder.builder().build()
			project.extensions.extraProperties.set("gitChartRepo.password", givenPassword)
			resultPassword = new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().password
		})

		expect:
		resultPassword == givenPassword
	}

	def "should resolve git password from env variable if set"() {
		given:
		def givenPassword = "pass-from-envs2"
		def resultPassword = ""
		environmentVariables.set("HELM_CHART_PUBLISH_GIT_REPO_PASSWORD", givenPassword).execute(() -> {
			Project project = ProjectBuilder.builder().build()
			resultPassword = new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().password
		})

		expect:
		resultPassword == givenPassword
	}

	def "should resolve git password to null if not set"() {
		expect:
		new GitChartRepositoryPropertiesProvider(ProjectBuilder.builder().build(), extensionWithAnyParams()).provide().password == null
	}

}
