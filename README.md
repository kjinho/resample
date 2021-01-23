# resample

A utility to resample the images of a PDF to generate a new PDF.

## Installation

- Install a java runtime environment, such as from [AdoptOpenJDK](https://adoptopenjdk.net/).
- Download a [standalone JAR release](https://github.com/kjinho/resample/releases) of ```resample```
  and save it in the location of your PDF files
- OPTIONAL: For a system wide installation,
  - save the [standalone JAR release](https://github.com/kjinho/resample/releases) in an appropriate
    directory (e.g., in UNIX-like systems, /usr/local/share/resample/),
  - save the [optional startup ruby script](https://github.com/kjinho/resample/blob/master/bin/resample)
    someplace in your $PATH (e.g., in UNIX-like systems, /usr/local/bin/),
  - modify the ruby script to reflect the correct locations of the java runtime and the
    standalone JAR, and
  - set the proper permissions for the ruby script, such as with the following command:
    ```$ chmod 711 /usr/local/bin/resample```

## Usage

Calling the jar directly using a Java runtime:

    $ java -jar resample-0.1.0-standalone.jar --input [INPUT_FILE] --output [OUTPUT_FILE] [options]
    
Calling the jar indirectly using an 
[optional startup ruby script](https://github.com/kjinho/resample/blob/master/bin/resample) 
to start java

    $ resample --input [INPUT_FILE] --output [OUTPUT_FILE] [options]

## Options

    -i, --input INPUT_FILE         input file name
    -o, --output OUTPUT_FILE       output file name
    -d, --dpi NUMBER          200  DPI
    -v                             verbosity level (display progress)
    -c, --color TYPE          BW   the color type [BW|GRAY|RGB|ALPHA]
    -h, --help

## Examples

...

### Bugs

...

## License

Copyright © 2021 Jin-Ho King

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at---

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.