# jumiru-bech32m

### Usage:
There are two modes this application can run in, encode and decode, which have a slight difference in their usage.

required verbatim, **required group**, *required parameter*, [optional verbatim], (tuple)  

------------

jumiru encode **data_format input_destination output_destination** *HRP*

jumiru decode **data_format input_destination output_destination** [errdetect]

------------

**data_format** = base64/bin/hex

**input_destination** = stdin/(file *file_name*)/(arg *data_part*)/emptyarg

**output_destination** = stdout/(file *file_name*)

### Example usage

* Encode "a" in HRP with empty data part in hex read from argument and output it to stdout
  
  `encode hex arg "" stdout a` or `encode hex emptyarg stdout a`


* Encode "b" in HRP with binary data part from argument and output it to file

  `encode bin arg 0110101 file "C:\bech32mdata\output1.txt" b`


* Encode "a" in HRP with base64 data part from "C:\bech32mdata\input1.txt" file into stdout

  `encode base64 file "C:\bech32mdata\input1.txt" stdout a`


* Decode A1LQFN3A read from argument to base64, output to stdout.
  
  `decode base64 arg A1LQFN3A stdout`


* Decode data from "C:\bech32mdata\input2.txt" to binary data and enable error detection, output to stdout.

  `binary binary file "C:\bech32mdata\input2.txt" stdout`