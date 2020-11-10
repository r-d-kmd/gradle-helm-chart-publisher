package dk.kmd.helm.chart.publish.properties

import static dk.kmd.helm.chart.publish.properties.TestDataProvider.emptyExtension
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.extensionWithAnyParams
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomName
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomUrl
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.contrib.java.lang.system.EnvironmentVariables
import spock.lang.Specification

class GitChartRepositoryPropertiesProviderTest extends Specification {

	@Rule
	public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

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
		environmentVariables.set("HELM_CHART_PUBLISH_GIT_REPO_USERNAME", randomName())
		Project project = ProjectBuilder.builder().build()
		project.extensions.extraProperties.set("gitChartRepo.username", givenUsername)

		expect:
		new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().username == givenUsername
	}

	def "should resolve git username from env variable if set"() {
		given:
		def givenUsername = randomName()
		environmentVariables.set("HELM_CHART_PUBLISH_GIT_REPO_USERNAME", givenUsername)
		Project project = ProjectBuilder.builder().build()

		expect:
		new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().username == givenUsername
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
		def givenPassword = randomAlphabetic(8)
		environmentVariables.set("HELM_CHART_PUBLISH_GIT_REPO_PASSWORD", randomAlphabetic(8))
		Project project = ProjectBuilder.builder().build()
		project.extensions.extraProperties.set("gitChartRepo.password", givenPassword)

		expect:
		new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().password == givenPassword
	}

	def "should resolve git password from env variable if set"() {
		given:
		def givenPassword = randomAlphabetic(8)
		environmentVariables.set("HELM_CHART_PUBLISH_GIT_REPO_PASSWORD", givenPassword)
		Project project = ProjectBuilder.builder().build()

		expect:
		new GitChartRepositoryPropertiesProvider(project, extensionWithAnyParams()).provide().password == givenPassword
	}

	def "should resolve git password to null if not set"() {
		expect:
		new GitChartRepositoryPropertiesProvider(ProjectBuilder.builder().build(), extensionWithAnyParams()).provide().password == null
	}

	def "should resolve git chart repository work directory from properties"() {
		given:
		def givenWorkDir = randomAlphabetic(4)
		Project project = ProjectBuilder.builder().build()
		project.extensions.extraProperties.set("gitChartRepo.workDir", givenWorkDir)
		def extension = extensionWithAnyParams(gitChartRepoWorkDir: randomAlphabetic(4))

		expect:
		new GitChartRepositoryPropertiesProvider(project, extension).provide().chartRepoWorkDir == givenWorkDir
	}

	def "should resolve git chart repository work directory from extension"() {
		given:
		def givenWorkDir = randomAlphabetic(4)
		Project project = ProjectBuilder.builder().build()
		def extension = extensionWithAnyParams(gitChartRepoWorkDir: givenWorkDir)

		expect:
		new GitChartRepositoryPropertiesProvider(project, extension).provide().chartRepoWorkDir == givenWorkDir
	}

	def "should resolve to empty if git chart repository work directory not set"() {
		given:
		def extension = extensionWithAnyParams(gitChartRepoWorkDir: null)

		expect:
		new GitChartRepositoryPropertiesProvider(ProjectBuilder.builder().build(), extension).provide().chartRepoWorkDir == ""
	}
}
