# jumiru-bech32m

There are two modes this application can run in, encode and decode, which have a slight difference in their usage.

For security reasons, running the application in its own dedicated directory it is highly recommended (or specifying its working directory to a one dedicated to this application). Should it be run by an untrusted user, it must be ensured that the user cannot set JVM arguments.


### Usage:

required verbatim, **required group**, *required parameter*, [optional verbatim], (tuple)  

---

java -jar jumiru-1.0.jar encode **data_format input_destination output_destination** *HRP*

java - jar jumiru-1.0.jar decode **data_format input_destination output_destination** [errdetect]

---

**data_format** = base64/bin/hex

**input_destination** = stdin/(file *file_name*)/(arg *data_part*)/emptyarg

**output_destination** = stdout/(file *file_name*)

<br>

*Note: Input from stdin must  be terminated by a newline*

*Note: file_name must be in the current working directory to avoid arbitrary file writes/reads*

### Example usage

* Encode "a" in HRP with empty data part in hex read from argument and output it to stdout
  
  `encode hex arg "" stdout a` or `encode hex emptyarg stdout a`


* Encode "b" in HRP with binary data part from argument and output it to file

  `encode bin arg 0110101 file "output1.txt" b`


* Encode "a" in HRP with base64 data part from "input1.txt" file into stdout

  `encode base64 file "input1.txt" stdout a`


* Decode A1LQFN3A read from argument to base64, output to stdout.
  
  `decode base64 arg A1LQFN3A stdout`


* Decode data from "input2.txt" to binary data and enable error detection, output to stdout.

  `binary binary file "input2.txt" stdout`