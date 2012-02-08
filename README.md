mp3renamer
----------
<i>A simple utility for renaming mp3 files based on information from the [ID3](http://en.wikipedia.org/wiki/ID3) tag.</i>

## Description
mp3renamer retrieves the ID3 tags of mp3 files and uses track, title, album, year and artist information form the tag to properly change the mp3's filename. The containing album folder is also renamed and moved to a corresponding artist folder. The renaming rules are described in the following section. The files to be renamed must be in separate folders according to their albums in the top level folder hierarchy. The opposite functionality is also supported, where the ID3 tag is written from the file name. Again, the files' hierarchy must be structured according to the naming rules described below.

## Naming rules
The rules for renaming files are simple.
<ul>
<li>
The folder hierarchy must be <p>/&lt;artist&gt;/(&lt;year&gt;) &lt;album&gt;/&lt;filename&gt;</p>
</li>
<li>&lt;year&gt; is in the 'YYYY' format.</li>
<li>
The filename must be of the form <p>&lt;track&gt;. &lt;track_title&gt;.mp3</p>
</li>
<li>&lt;track&gt; is the order number of the track formatted to two digits.</li>
<li>Every word in <p>&lt;track_title&gt;, &lt;artist&gt;, &lt;album&gt;</p> must begin with a capital letter and all words must be separated by a single space character.</li>
<li>All special characters are converted to the character '_'.</li>
</ul>

## Write operation
The app can execute in 'write operation' and fill in the ID3 tag of the mp3 file with information gathered from the folder hierarchy and the filename. It is assumed that the file is already named following the above rules.

## Usage
mp3renamer <i>[-vw] directory</i>
<dl>
<dt>v</dt><dd>Verbose mode. Prints informational messages.</dd>
<dt>w</dt><dd>Write mode. Writes ID3 tags <b>from</b> filenames.</dd>
<dt>directory</dt><dd>The directory containing the mp3 files. Each album must be in a top level folder of its own inside this directory.</dd>
</dl>

## Dependencies
The source depends on the [JAudioTagger](http://www.jthink.net/jaudiotagger/) v2.0.4 library for accessing ID3 tags and the [Apache Commons Lang](http://commons.apache.org/lang/) v3.1 library for string manipulation utilities.

## DISCLAIMER
*This app is used to help __ME__ organize my music collection. It has not been thoroughly tested and is in no way guaranteed to work. Use at your own risk, loss of data may occur.*
