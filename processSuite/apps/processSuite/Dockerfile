FROM mcr.microsoft.com/dotnet/sdk:6.0

RUN apt-get -q update && \
  curl -fsSL https://deb.nodesource.com/setup_16.x | bash - && \
  apt-get install -y \
  nodejs

WORKDIR /apps/processSuite/frontend
COPY apps/processSuite/frontend/package.json apps/processSuite/frontend/package-lock.json ./
RUN npm i

WORKDIR /
COPY ../../analyzers.ruleset .

WORKDIR /apps/processSuite
COPY apps/processSuite/processSuite.csproj .
COPY apps/processSuite/processSuite.props .
RUN dotnet restore
