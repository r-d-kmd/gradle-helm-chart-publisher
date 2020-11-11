FROM cirocosta/gitserver-http:formatting

RUN cd /var/lib/git && git init --bare --shared=true chart-repo.git
CMD [ "-start" ]
