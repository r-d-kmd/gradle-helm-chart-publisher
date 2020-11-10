FROM jkarlos/git-server-docker

RUN mkdir repos && mkdir repos/chart-repository
RUN passwd -d git \
  && sed -i 's/^PasswordAuthentication.*/PasswordAuthentication yes/g' /etc/ssh/sshd_config \
  && echo "PermitEmptyPasswords yes" >> /etc/ssh/sshd_config \
  && ls /etc/init.d

RUN git config --global user.email "mail@example.com"

RUN cd repos/chart-repository \
  && git init --bare --shared=true

RUN sh /etc/init.d/sshd restart

CMD ["sh", "start.sh"]
