package dk.kmd.helm.chart.publish.properties

import static dk.kmd.helm.chart.publish.properties.TestDataProvider.extensionWithAnyParams
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class WorkDirPropertiesProviderTest extends Specification {

	def "should resolve git chart repository work directory from properties"() {
		given:
		def givenWorkDir = randomAlphabetic(4)
		Project project = ProjectBuilder.builder().build()
		project.extensions.extraProperties.set("gitChartRepo.workDir", givenWorkDir)
		def extension = extensionWithAnyParams(gitChartRepoWorkDir: randomAlphabetic(4))

		when:
		def result = new WorkDirPropertiesProvider(project, extension).provide()

		then:
		result.chartRepoWorkingDirectory == "${result.chartRepoTmpDirectory}/${givenWorkDir}"
	}

	def "should resolve git chart repository work directory from extension"() {
		given:
		def givenWorkDir = randomAlphabetic(4)
		Project project = ProjectBuilder.builder().build()
		def extension = extensionWithAnyParams(gitChartRepoWorkDir: givenWorkDir)

		when:
		def result = new WorkDirPropertiesProvider(project, extension).provide()

		then:
		result.chartRepoWorkingDirectory == "${result.chartRepoTmpDirectory}/${givenWorkDir}"
	}

	def "should resolve to empty if git chart repository work directory not set"() {
		given:
		def extension = extensionWithAnyParams(gitChartRepoWorkDir: null)


		when:
		def result = new WorkDirPropertiesProvider(ProjectBuilder.builder().build(), extension).provide()

		then:
		result.chartRepoWorkingDirectory == "${result.chartRepoTmpDirectory}/"
	}
}
