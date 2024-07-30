# GitHub Repository Fetcher

## Table of Contents
- [Overview](#overview)
- [Instalation](#Installation)
- [Technologies Used](#technologies-used)
- [Usage](#usage)
- [Contact](#contact)

## Overview

This project provides an API to check if a user exists. In case a user with the given name exists, it provides information about his repositories, such as branches that are not fork and sha (Secure Hash Algorithm) of the last commit.

## Installation

Steps to install the project:

I. Download the reposiotry or the newest release form github. 

II. Make sure that u have java in version 21 install on your PC!

Application should start properly.

## Technologies Used

- Java
- Spring Boot
- WebFlux
- REST API

## Usage 

To retrieve information from the GitHub API, you can use a testing program like POSTMAN or use Swagger on a local host on port 8080.

If user exist, the response will be like example below:
```json
[
  {
    "repoName": "GetGitHubRepos",
    "ownerLogin": "Sekus96",
    "branches": [
      {
        "branchName": "master",
        "latestCommitSha": "d05e199d520600daab2d844689d287a59abbf18a"
      }
    ]
  },
  {
    "repoName": "CRUDApp",
    "ownerLogin": "Sekus96",
    "branches": [
      {
        "branchName": "master",
        "latestCommitSha": "bdf855a4996ef36e9985f00e4d69f542e4d350b5"
      }
    ]
  }
]
```

In case that user does not exist, application will return:

```json
{
  "status": 404,
  "message": "User UserDoesNotExist does not exist."
}
```

## Contact

For any questions or feedback, please reach out to me:
**Email**: [radsek.96@gmail.com](mailto:radsek.96@gmail.com)
