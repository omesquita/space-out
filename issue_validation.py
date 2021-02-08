import re
import sys

import requests

REPO_API = "https://api.github.com/repos"
PROJECT_PATH = "/omesquita/space-out"

headers = {
    'Authorization': 'Token 8cf33eed7fa3ec3a3d6c6509c33d47cf41c509d9',
    'Accept': 'application/vnd.github.v3+json'
}

payload = {'status': 'open', 'labels': 'bugfix'}
issue_endpoint = f'{REPO_API}{PROJECT_PATH}/issues'


def get_jira_id_from_title(title: str):
    print(f'Get jira id from title pr {title}')
    match = re.search(r'(^\[\w+\])\[(\w+-\d+)\]', title)
    try:
        return match.group(2)
    except AttributeError:
        return ""


def check_if_issue_has_same_jira_id(issue_title: str, jira_id: str):
    print(f'Check jira id {jira_id} in issue {issue_title}')
    match = re.search(r'(^\[(ANDROID|IOS)\])\[(\w+-\d+)\]', issue_title)
    try:
        return match.group(3) == jira_id
    except AttributeError:
        return False


def check_exists_issue_with_jira_id(jira_id: str):
    res = requests.get(issue_endpoint, headers=headers, params=payload)
    json_result = res.json()

    for issue in json_result:
        title = issue['title']
        if 'pull_request' not in issue and check_if_issue_has_same_jira_id(title, jira_id):
            exit()
        else:
            exit(f'Issue not exists to jira id {jira_id}')


def check_pull_request(title: str):
    jira_key = get_jira_id_from_title(title)

    if jira_key == "":
        exit("Jira Key not found")

    check_exists_issue_with_jira_id(jira_key)


title_param = sys.argv[1]

check_pull_request(title_param)
