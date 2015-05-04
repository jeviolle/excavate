/*# Copyright (C) 2015, Rick Briganti
#
# This file is part of excavate
#
# excavate is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#*/

package org.newaliases.excavate

import groovyx.net.http.*

import static groovy.io.FileType.FILES
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class Search {
    def final central = "https://search.maven.org"
    def final searchBase = "${central}/solrsearch/select"
    def format = "json"

    def localArtifacts() {
        def pr = new PomReader()
        def repo = new SettingsParser().getRepositoryLocation()
        println "REPO: ${repo}"
        try {
            new File(repo).eachFileRecurse(FILES, pr.isPom)
            println "\n*** LOCAL ARTIFACTS ***\n"
            pr.displayGavMap()
        } catch (FileNotFoundException e) {
            println "${repo} - not found..exiting"
            System.exit(1)
        }
    }

    def buildUserAgent() {
        def osType = System.getProperty('os.name')
        def osVersion = System.getProperty('os.version')
        def javaVersion = System.getProperty('java.version')
        def groovyVersion = GroovySystem.getVersion()
        def uaString = "Excavate/1.0"

        uaString += " (${osType}/${osVersion}; Java/${javaVersion}; Groovy/${groovyVersion})"
        return uaString
    }

    private def getRows(q) {

        def http = new HTTPBuilder(central)

        http.request(GET, JSON) {
            uri.path = searchBase

            if (q.contains('&')) {
                uri.query = [q: q.split('&')[0], wt: format, rows: 0, core: "gav"]
            } else {
                uri.query = [q: q, wt: format, rows: 0]
            }

            headers.'User-Agent' = buildUserAgent()

            response.success = { resp, json ->
                return json.response.numFound
            }

            response.failure = { resp ->
                println "Failed to get intial row count." +
                        "Unexpected error: ${resp.statusLine}"
                System.exit(1)
            }
        }
    }

    def sha1(String q) {
        this.query('1:' + "" + q + "")
    }

    def artifactId(String q) {
        this.query('a:' + '"' + q + '"')
    }

    def groupId(String q) {
        this.query('g:' + '"' + q + '"')
    }

    def ga(String q) {
        def (groupId, artifactId) = q.split(',')
        this.query('g:' + "" + groupId + ' AND a:' + "" + artifactId + "" + '&core=gav')
    }

    def className(String q) {
        this.query('c:' + '"' + q + '"')
    }

    def fullClassName(String q) {
        this.query('fc:' + '"' + q + '"')
    }

    def tag(String q) {
        this.query('tags:' + '"' + q + '"')
    }

    def version(String q) {
        this.query('v:' + '"' + q + '"')
    }

    def basic(String q) {
        this.query(q)
    }

    def query(q) {
        def http = new HTTPBuilder(central)
        def rowCount = getRows(q)
        def artifacts = [:]

        http.request(GET, JSON) {
            uri.path = searchBase

            if (q.contains('&')) {
                uri.query = [q: q.split('&')[0], wt: format, rows: rowCount, core: "gav"]
            } else {
                uri.query = [q: q, wt: format, rows: rowCount]
            }

            headers.'User-Agent' = buildUserAgent()

            response.success = { resp, json ->
                println resp.statusLine

                println "Found ${json.response.numFound} possibilities"
                println "\n*** CENTRAL ARTIFACTS ***\n"
                json.response.docs.each {
                    if (it['latestVersion']) {
                        println "${it['id']} [${it['latestVersion']}]"
                    } else {
                        def ga = it['g'] + ':' + it['a']
                        if (artifacts[ga].getClass() != ArrayList) {
                            artifacts[ga] = []
                        }
                        artifacts[ga].add(it['v'])
                    }

                }

                if (artifacts.size() > 0) {
                    artifacts.each { k, v ->
                        //def outString = ""
                        def outString = k + " ["
                        outString += v.each {
                            it
                        }.join(', ')
                        println "${outString}]"
                    }
                }
            }

            response.failure = { resp ->
                println "Failed to retrieve artifact information." +
                        "Unexpected error: ${resp.statusLine}"
            }
        }
    }

      // TODO - grabs descriptions if requested
/*    def getDescription(p) {
        def http = new HTTPBuilder(central)
        http.request(GET, JSON) {
            // ?filepath=com/jolira/guice/3.0.0/guice-3.0.0.pom
            uri.path = "${central}/remotecontent"
            uri.query = [filepath: path]

            response.success = { resp, json ->
                println resp.statusLine
            *//*
                println "Found ${json.response.numFound} possibilities\n"
                println json.response.docs.each {
                    println it['id']
                }
            *//*
            }

            response.failure = { resp ->
                println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
            }

        }
    }*/
}
