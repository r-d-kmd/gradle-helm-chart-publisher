package dk.kmd.helm.chart.publish

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

class TestProject {

	String projectVersion
	String chartVersion
	String projectName
	String chartName
	String gitChartRepoUrl
	File projectRoot

	TestProject prepare() {
		prepareBuildFile()
		prepareProjectSettingsFile()
		prepareProjectChart()
		return this
	}

	void prepareBuildFile() {
		newFile("build.gradle") << """
 		plugins {
            id 'dk.kmd.helm.chart.publish'
        }
        helmChartPublisher {
            gitChartRepoUrl = "${gitChartRepoUrl}"
        }
        project.version = "${projectVersion}"
        """
	}

	void prepareProjectChart() {
		def chartFolder = newFolder("helm", chartName)
		def chartFile = newFile(chartFolder, "Chart.yaml")
		chartFile << "apiVersion: v1\ndescription: Example chart\nname: ${chartName}\nversion: ${chartVersion}"
	}

	void prepareProjectSettingsFile() {
		newFile("settings.gradle") << """
			rootProject.name = '${projectName}'
		"""
	}

	GradleRunner gradle() {
		return GradleRunner.create()
			.withDebug(true)
			.withProjectDir(projectRoot)
			.withPluginClasspath()
	}

	BuildResult runGradle(String... arguments) {
		return gradle().withArguments(arguments).build()
	}

	private File newFile(def name) {
		return newFile(projectRoot, name)
	}

	private File newFile(File directory, def name) {
		directory.mkdirs()
		def file = new File(directory, name)
		file.createNewFile()
		return file
	}

	private File newFolder(String... folderNames) throws IOException {
		File file = projectRoot
		for (int i = 0; i < folderNames.length; i++) {
			String folderName = folderNames[i];
			file = new File(file, folderName);
			if (!file.mkdir() && i == folderNames.length - 1) {
				throw new IOException(
					"a folder with the name \'" + folderName + "\' already exists");
			}
		}
		return file;
	}

}
