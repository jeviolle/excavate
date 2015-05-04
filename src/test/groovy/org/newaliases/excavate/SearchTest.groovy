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

// TODO - finish tests
class SearchTest extends GroovyTestCase {
    void testSha1() {
        new Search().sha1('35379fb6526fd019f331542b4e9ae2e566c57933')
    }

    void testArtifactId() {
        new Search().artifactId('http-builder')
    }

    void testGroupId() {
        new Search().groupId('com.google.inject')
    }

    void testClassName() {
        new Search().className('junit')
    }

    void testFullClassName() {
        new Search().fullClassName('org.specs.runner.JUnit')
    }

    void testTag() {
        new Search().tag('sbtVersion-0.11')
    }

    void testVersion() {
        new Search().version('0.7')
    }

    void testBasic() {
        new Search().basic('com.google.inject')
    }

    void testGA() {
        new Search().ga('com.google.inject,guice')
    }
}
