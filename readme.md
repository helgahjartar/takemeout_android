# Til að Keyra app:

1. Keyra upp þjónustu, takemeout_service
2. Finna local ip tölu sem router assignar tölvunni ykkar, skipun:
	
```
$ ifconfig | gerp inet
```

Það koma upp nokkrar línur, ein lítur sirka svona út:

```
inet 192.168.1.36 netmask 0xffffff00 broadcast 192.168.1.255
```     
Taka ip fremst í línunni.
  
3. Skipta út ip í skrám EventDetailsActivity, lína 21 og EventOverviewActivity, lína 19 fyrir ip töluna sem þið funduð. Ekki taka út port.

4. Keyra project gegnum android studio
