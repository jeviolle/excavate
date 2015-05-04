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

class Main {

    static void main(String[] args) {
        def cli = new CliBuilder(usage: 'excavate [option]', stopAtNonOption: false)
        cli.h(argName: 'help', longOpt: 'help', 'Show usage information and quit')
        cli.l(argName: 'local', longOpt: 'local', 'local .m2 artifacts (returns all)')
        cli.b(argName: 'basic', longOpt: 'basic', args: 1, 'matches group and artifact ids (returns latest)')
        cli.a(argName: 'artifact', longOpt: 'artifact', args: 1, 'artifact id match')
        cli.g(argName: 'group', longOpt: 'group', args: 1, 'group id match')
        cli.ga(argName: 'groupId,artifactId', longOpt: 'ga', args: 1, 'matches group and artifact id (returns all)')
        cli.c(argName: 'classname', longOpt: 'classname', args: 1, 'by classname')
        cli.fc(argName: 'fullclass', longOpt: 'fullclass', args: 1, 'by full classname')
        cli.t(argName: 'tag', longOpt: 'tag', args: 1, 'artifacts with the matching tag')
        cli.v(argName: 'version', longOpt: 'version', args: 1, 'artifacts with this version')
        cli.s(argName: 'sha1', longOpt: 'sha1', args: 1, 'artifacts that match the SHA1')
// TODO - support advanced coordinate searching

        def opt

        if ('-h' in args || '--help' in args || !args) {
            cli.usage()
            System.exit(0)
        } else {
            opt = cli.parse(args)
        }

// Search local .m2 for downloaded artifacts
        if (opt.l) {
            new Search().localArtifacts()
        }

// Basic query - search both groupId and ArtifactId
// with most recent version
        if (opt.b) {
            new Search().basic(opt.basic)
        }

// Artifact query - returns matching artifactId's
// with most recent version released
        if (opt.a) {
            new Search().artifactId(opt.artifact)
        }

// Group query - returns all artifacts in specified group
// with most recent version
        if (opt.g) {
            new Search().groupId(opt.group)
        }

// GA query - returns all version for the match
        if (opt.ga) {
            new Search().ga(opt.ga)
        }

// Classname - returns artifacts w/ specific version containing
// a matching class name
        if (opt.c) {
            new Search().className(opt.classname)
        }

// Fully Qualified Classname - returns artifacts w/ specific version
        if (opt.fc) {
            new Search().fullClassName(opt.fullclass)
        }

// Tag search
        if (opt.t) {
            new Search().tag(opt.tag)
        }

// Version - returns artifacts that match this version
        if (opt.v) {
            new Search().version(opt.version)
        }

// SHA1 - returns artifacts matching the specified sums
        if (opt.s) {
            new Search().sha1(opt.sha1)
        }
    }
}