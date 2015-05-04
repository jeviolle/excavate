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

class SettingsParser {
    // TODO - interpolate location string if it contains variables
    def getRepositoryLocation() {
        def repo
        def m2Home = System.getenv("M2_HOME")
        def userHome = System.getenv("HOME")

        if (m2Home) {
            def globalSettings = new File("${m2Home}/conf/settings.xml")
            if (globalSettings.exists() && globalSettings.canRead()) {
                repo = new XmlParser().parseText(globalSettings.text)['localRepository'].text()
            }
        }

        if (userHome) {
            def userSettings = new File("${userHome}/.m2/settings.xml")
            if (userSettings.exists() && userSettings.canRead()) {
                repo = new XmlParser().parseText(userSettings.text)['localRepository'].text()
            }
        }

        if (repo?.size() == 0 || repo == null) { repo = "${userHome}/.m2/repository" }

        return repo
    }
}
