# Plán reportu
*Sem průběžně pište, co vás napadne, že by se hodilo do reportu, ať ho pak máme z čeho dělat. Řekněme, že tady ten soubor je jen pro nás takže bych ho nechal v libovolném jazyku a formátu. Prostě sem si pište co chete a pak to smažeme.*

I managed to set up gpg signing on windows with git bash. I generated the kay with using this [tutorial](https://docs.github.com/en/authentication/managing-commit-signature-verification/generating-a-new-gpg-key) And then I followed the [lecture material](https://docs.github.com/en/authentication/managing-commit-signature-verification/signing-commits).

Also the Github CI was set up. I used the default file for CI with Maven, but modified it so that it runs for every push on every branch. The tests run on their own as part of the maven build stage so no additional command to run the tests is required.
