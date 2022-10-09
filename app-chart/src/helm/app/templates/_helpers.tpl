{{- define "app.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version }}
{{- end }}
