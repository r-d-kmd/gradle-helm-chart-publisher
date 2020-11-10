package dk.kmd.helm.chart.publish.infrastructure

import org.gradle.api.logging.Logger

class Retryable {

	int numberOfTries
	Logger logger

	Retryable(int numberOfTries, Logger logger) {
		this.numberOfTries = numberOfTries
		this.logger = logger
	}

	def execute(Runnable runnable){
		int i = 0
		def success = false
		Exception exception = null
		while (i < numberOfTries && !success) {
			i++
			logger.info("$i retry to execute")

			try {
				runnable.run()
				success = true
				logger.info("Task successful")
			} catch (Exception e) {
				logger.warn("Error: ", e)
				logger.info("Task failed.")
				exception = e
			}
		}
		if (!success) {
			throw new Exception("Task failed.", exception)
		}
	}
}
