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

import org.apache.maven.model.io.xpp3.MavenXpp3Reader

class PomReader {
    def pomPattern = ~/\.pom$/
    def gavMap = [:]

    def isPom = { f ->
        def matcher = f =~ pomPattern
        if (matcher.getCount() > 0) {
            gavDetails f
        }
    }

    def gavDetails = { p ->
        def groupId
        def artifactId
        def version

        def f = new FileReader(p)
        def pomReader = new MavenXpp3Reader()
        def model = pomReader.read(f)
        f.close()

        if (model?.getGroupId() != null) {
            groupId = model.getGroupId()
        } else {
            groupId = model.getParent().getGroupId()
        }

        artifactId = model.getArtifactId()

        if (model.getVersion() != null) {
            version = model.getVersion()
        } else {
            version = model.getParent().getVersion()
        }

        if (gavMap[groupId].getClass() != LinkedHashMap) {
            gavMap[groupId] = [:]
        }

        if (gavMap[groupId][artifactId].getClass() != ArrayList) {
            gavMap[groupId][artifactId] = []
        }

        gavMap[groupId][artifactId].push version
    }

    def displayGavMap() {
        gavMap.keySet().each { g ->
            gavMap[g].each { k, v ->
                println "${g},${k} (${v.unique().join(', ')})"
            }
        }
    }
}
