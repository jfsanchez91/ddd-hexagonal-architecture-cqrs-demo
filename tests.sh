#!/usr/bin/env bash
set -euo pipefail

assert_not_empty() {
    local value=$1
    local name=$2
    if [[ -z "$value" ]]; then
        echo "ERROR: $name is empty!"
        exit 1
    fi
}

pause() {
    local msg=${1:-"Press Enter to continue..."}
    read -r -p "$msg"
}

echo "# create teams"
team_1_id=$(curl -s -XPOST localhost:8080/api/v1/teams \
  -H 'Content-Type: application/json' \
  -d '{"name": "team-1"}' | jq -r '.teamId')
assert_not_empty "$team_1_id" "team_1_id"

team_2_id=$(curl -s -XPOST localhost:8080/api/v1/teams \
  -H 'Content-Type: application/json' \
  -d '{"name": "team-2"}' | jq -r '.teamId')
assert_not_empty "$team_2_id" "team_2_id"

team_3_id=$(curl -s -XPOST localhost:8080/api/v1/teams \
  -H 'Content-Type: application/json' \
  -d '{"name": "team-3"}' | jq -r '.teamId')
assert_not_empty "$team_3_id" "team_3_id"

# empty team-4
team_4_id=$(curl -s -XPOST localhost:8080/api/v1/teams \
  -H 'Content-Type: application/json' \
  -d '{"name": "team-4"}' | jq -r '.teamId')
assert_not_empty "$team_4_id" "team_4_id"

echo "# list teams"
curl -s localhost:8080/api/v1/teams | jq
pause

echo "# create users"
user_1_id=$(curl -s -XPOST localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name": "user-1", "email": "user-1@example.com"}' | jq -r '.userId')
assert_not_empty "$user_1_id" "user_1_id"

user_2_id=$(curl -s -XPOST localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name": "user-2", "email": "user-2@example.com"}' | jq -r '.userId')
assert_not_empty "$user_2_id" "user_2_id"

user_3_id=$(curl -s -XPOST localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name": "user-3", "email": "user-3@example.com"}' | jq -r '.userId')
assert_not_empty "$user_3_id" "user_3_id"

user_4_id=$(curl -s -XPOST localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name": "user-4", "email": "user-4@example.com"}' | jq -r '.userId')
assert_not_empty "$user_4_id" "user_4_id"

echo "# list users"
curl -s localhost:8080/api/v1/users | jq
pause

echo "# add users to teams"
echo "# user-1 -> team-1 & team-2"
curl -s -XPOST localhost:8080/api/v1/teams/${team_1_id}/users \
  -H 'Content-Type: application/json' \
  -d "{\"userId\":\"${user_1_id}\",\"alias\":\"user-1-in-team-1\"}" | jq
curl -s -XPOST localhost:8080/api/v1/teams/${team_2_id}/users \
  -H 'Content-Type: application/json' \
  -d "{\"userId\":\"${user_1_id}\",\"alias\":\"user-1-in-team-2\"}" | jq
pause

echo "# user-2 -> team-1 & team-3"
curl -s -XPOST localhost:8080/api/v1/teams/${team_1_id}/users \
  -H 'Content-Type: application/json' \
  -d "{\"userId\":\"${user_2_id}\",\"alias\":\"user-2-in-team-1\"}" | jq
curl -s -XPOST localhost:8080/api/v1/teams/${team_3_id}/users \
  -H 'Content-Type: application/json' \
  -d "{\"userId\":\"${user_2_id}\",\"alias\":\"user-2-in-team-3\"}" | jq
pause

echo "# user-3 -> team-1, team-2, team-3"
curl -s -XPOST localhost:8080/api/v1/teams/${team_1_id}/users \
  -H 'Content-Type: application/json' \
  -d "{\"userId\":\"${user_3_id}\",\"alias\":\"user-3-in-team-1\"}" | jq
curl -s -XPOST localhost:8080/api/v1/teams/${team_2_id}/users \
  -H 'Content-Type: application/json' \
  -d "{\"userId\":\"${user_3_id}\",\"alias\":\"user-3-in-team-2\"}" | jq
curl -s -XPOST localhost:8080/api/v1/teams/${team_3_id}/users \
  -H 'Content-Type: application/json' \
  -d "{\"userId\":\"${user_3_id}\",\"alias\":\"user-3-in-team-3\"}" | jq
pause

echo "# list users"
curl -s localhost:8080/api/v1/users | jq
pause

echo "# list teams"
curl -s localhost:8080/api/v1/teams | jq
pause
