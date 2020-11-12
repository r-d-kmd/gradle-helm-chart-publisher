# gradle-helm-chart-publisher
[![Build Status](https://travis-ci.org/r-d-kmd/gradle-helm-chart-publisher.svg?branch=main)](https://travis-ci.org/r-d-kmd/gradle-helm-chart-publisher)

_gradle package helm chart and publish to git repository plugin_ 

Helm chart is an integral part of a project, more often its definition is placed as closest to code as possible. 

In order to be able to develop both at the same time helm allows to do [chart versioning](https://v2.helm.sh/docs/developing_charts/#charts-and-versioning).

This plugin packages the chart and leverages the possibility of using git repository as a [helm repository](https://helm.sh/docs/topics/chart_repository/). 

## Basic usage

```
plugins {
     id 'dk.kmd.helm.chart.publish'
 }
 helmChartPublisher {
    gitChartRepoUrl = https://github.com/path/to-your-repo.git"
 }
```

```
$ ./gradlew releaseHelmChart
```

## Requirements


Plugin requires helm client to be installed.

## Workflow

- clone git repository where helm chart definitions are stored
- init helm client
- package chart
- reindex helm chart repository
- push changes to git repository 

## Parameters


Parameter | Description | Default value |  Flag | Environment variable | build.gradle property 
--------- | ----------- | ------------- | ----- | -------------------- | ---------------------
Git chart repository URL | URL to a git repository in which helm charts are stored. Accepts both ssh and https protocols. | - | `gitChartRepo.url` | - | `helmChartPublisher.gitChartRepoUrl`
Git username | Git repository username | - | `gitChartRepo.username` | `HELM_CHART_PUBLISH_GIT_REPO_USERNAME` | `helmChartPublisher.gitUsername`
Git password | Git repository password | - | `gitChartRepo.password` | `HELM_CHART_PUBLISH_GIT_REPO_PASSWORD` | -
Git repository working directory | Directory in git repository where helm chart repository is stored | / (root folder of the repository) | `gitChartRepo.workDir` | - | `helmChartPublisher.gitChartRepoWorkDir`
Chart version | Version under which the chart will be published | project.version |  `chart.version` | - | -
Chart name | Version under which the chart will be published | project.name |  `chart.name` | - | `helmChartPublisher.chartName`
Chart definition directory | Where in the project chart definition is stored | `${projectDir}/helm/$chartName` | `chartDefinition.dir` | - | `helmChartPublisher.chartDefinitionDir`
