# executor

The RESTlet-based cluter job executor and monitor

[![Build Status](https://travis-ci.org/piret-rna/executor.svg?branch=master)](https://travis-ci.org/piret-rna/executor) 
[![codecov.io](http://codecov.io/github/piret-rna/executor/coverage.svg?branch=master)](http://codecov.io/github/piret-rna/executor?branch=master)

### 0.0 An API

 * `POST` -- run a new job, the username & command line are required, required resources specification is optional
 * `GET` -- get the job(s) as JSON, the jobID is required
 * `DLETE` -- interrupt a running job, the jobID is required
 
### 0.1. REST URLSs
 
 * `GET /jobs` - get the list of currently active jobs
 * `GET /jobs?start=01-01-2012&end=01-31-2012` - get the list of ALL jobs for a date range
 
 * `GET /jobs?user=psenin` - get the list of _active_ jobs for the user psenin
 * `GET /jobs?user=psenin&start=01-01-2012&end=01-31-2012` - get the list of _active_ jobs for the user psenin and the date range
 
 * `GET /jobs/{jobid}` -- for getting the specific job as a JSON of the [ClusterJob](https://github.com/piret-rna/executor/blob/master/src/main/java/net/seninp/executor/resource/ClusterJob.java)
 * `POST /jobs` - schedule a new job for running using JSON of the [ClusterJob](https://github.com/piret-rna/executor/blob/master/src/main/java/net/seninp/executor/resource/ClusterJob.java)
 * `DELETE /jobs/{jobid}` -- for stopping the job with a specific jobid
