package dk.kmd.helm.chart.publish.properties

import static dk.kmd.helm.chart.publish.properties.TestDataProvider.emptyExtension
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.extensionWithAnyParams
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomName
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomDir
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomVersion

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class ChartPropertiesProviderTest extends Specification {

	def "should resolve chart version from property if set"() {
		given:
		def givenVersion = "2.0.0"
		Project project = ProjectBuilder.builder().build()
		project.version = randomVersion()
		project.extensions.extraProperties.set("chart.version", givenVersion)
		expect:
		new ChartPropertiesProvider(project, extensionWithAnyParams()).provide().chartVersion == givenVersion
	}

	def "should fall back to project version if chart.version property not set"() {
		given:
		def givenVersion = "2.0.1"
		Project project = ProjectBuilder.builder().build()
		project.version = givenVersion
		expect:
		new ChartPropertiesProvider(project, extensionWithAnyParams()).provide().chartVersion == givenVersion
	}

	def "should resolve chart name from property if set"() {
		given:
		def givenName = randomName()
		Project project = ProjectBuilder.builder().withName(randomName()).build()
		project.extensions.extraProperties.set("chart.name", givenName)
		def extension = extensionWithAnyParams(chartName: randomName())

		expect:
		new ChartPropertiesProvider(project, extension).provide().chartName == givenName
	}

	def "should resolve chart name from extension if set and property is missing"() {
		given:
		def givenName = randomName()
		Project project = ProjectBuilder.builder().withName(randomName()).build()
		def extension = extensionWithAnyParams(chartName: givenName)

		expect:
		new ChartPropertiesProvider(project, extension).provide().chartName == givenName
	}

	def "should fall back to project.name for chart name when extension and property is missing"() {
		given:
		def givenName = randomName()
		Project project = ProjectBuilder.builder().withName(givenName).build()

		expect:
		new ChartPropertiesProvider(project, emptyExtension()).provide().chartName == givenName
	}

	def "should resolve chart definition path from property if set"() {
		given:
		def givenDir = randomDir()
		Project project = ProjectBuilder.builder().build()
		project.extensions.extraProperties.set("chartDefinition.dir", givenDir)
		def extension = extensionWithAnyParams(chartDefinitionDir: randomDir())

		expect:
		new ChartPropertiesProvider(project, extension).provide().chartDefinitionDir == givenDir
	}

	def "should resolve chart definition path from extension if set and property is missing"() {
		given:
		def givenDir = randomDir()
		Project project = ProjectBuilder.builder().build()
		def extension = extensionWithAnyParams(chartDefinitionDir: givenDir)

		expect:
		new ChartPropertiesProvider(project, extension).provide().chartDefinitionDir == givenDir
	}

	def "should fall back to a default in project.buildDir for chart name when extension and property is missing"() {
		given:
		Project project = ProjectBuilder.builder().build()

		when:
		def chartProperties = new ChartPropertiesProvider(project, emptyExtension()).provide()

		then:
		chartProperties.chartDefinitionDir == "${project.projectDir}/helm/${chartProperties.chartName}"
	}
}
