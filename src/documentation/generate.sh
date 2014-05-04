path=$(dirname $0);

cp $path/main.html $path/result.html
redcarpet --parse-fenced-code-blocks $path/main.md >> $path/result.html
echo "</body></html>" >> $path/result.html

exit 0
