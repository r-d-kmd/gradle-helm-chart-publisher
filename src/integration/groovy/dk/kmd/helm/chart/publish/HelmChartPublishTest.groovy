package dk.kmd.helm.chart.publish

import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomName
import static dk.kmd.helm.chart.publish.properties.TestDataProvider.randomVersion

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.ajoberstar.grgit.Grgit
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.yaml.snakeyaml.Yaml
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class HelmChartPublishTest extends Specification {

	static final String GIT_CHART_REPO_URL = "http://localhost:8080/chart-repo.git"

	@Rule
	TemporaryFolder temporaryFolder = new TemporaryFolder()

	def "should init repository and publish multiple charts one by one"() {
		given:
		def givenProject = prepareProject(params)

		when:
		givenProject.runGradle("releaseHelmChart")

		then: "clone independent copy of git helm chart repository "
		def clonedRepoDir = "$temporaryFolder.root.path/chart-repo"
		new File(clonedRepoDir).deleteDir()
		Grgit.clone(uri: GIT_CHART_REPO_URL, dir: clonedRepoDir)

		and:
		"packaged repo should be in the repository in ${params.chartName} folder"
		def repositoryFiles = new File("$clonedRepoDir/$params.chartName").listFiles()
		repositoryFiles.name.any { it == "${params.chartName}-${params.chartVersion}.tgz" }

		and: "chart entry should be listed in the index"
		def index = new Yaml().load(new File("$clonedRepoDir/index.yaml").text)
		index.entries[params.chartName] != null
		index.entries[params.chartName].version.any { it == params.chartVersion }

		and: "repository package file exists under specified location"
		def chartPathInRepository = index.entries[params.chartName].urls.flatten().first()
		new File("${clonedRepoDir}/${chartPathInRepository}").exists()

		where:
		params << (1..3).collect { randomProjectValues() }
	}

	def "should init repository and save charts from multiple projects publishing at the same time"() {
		given:
		def givenProjects = (1..3).collect { prepareProject(randomProjectValues()) }
		ExecutorService executorService = Executors.newCachedThreadPool()

		when:
		executorService.invokeAll(givenProjects.collect { project ->
			({ project.runGradle("releaseHelmChart") } as Callable)
		})

		then: "clone independent copy of git helm chart repository"
		def clonedRepoDir = "$temporaryFolder.root.path/chart-repo"
		new PollingConditions(timeout: 5).eventually {
			new File(clonedRepoDir).deleteDir()
			Grgit.clone(uri: GIT_CHART_REPO_URL, dir: clonedRepoDir)
			assert new Yaml().load(new File("$clonedRepoDir/index.yaml").text).entries.keySet().containsAll(givenProjects.chartName)
		}

		and: "chart entry should be listed in the index"
		def index = new Yaml().load(new File("$clonedRepoDir/index.yaml").text)
		givenProjects.forEach { project ->
			def entry = index.entries[project.chartName]
			assert entry != null
			assert entry.version.any { it == project.chartVersion }
		}

		and: "package file exists under specified location"
		givenProjects.forEach { project ->
			def chartPathInRepository = index.entries[project.chartName].urls.flatten().first()
			assert chartPathInRepository != null
			assert new File("${clonedRepoDir}/${chartPathInRepository}").exists()
		}
	}

	def prepareProject(Map params = [:]) {
		return new TestProject(params << [projectRoot    : temporaryFolder.newFolder(params.projectName),
										  gitChartRepoUrl: GIT_CHART_REPO_URL]).prepare()
	}

	static Map randomProjectValues() {
		def name = randomName()
		def version = randomVersion()
		[projectName: name, chartName: name, projectVersion: version, chartVersion: version]
	}
}
