# Git structure

We will follow a variation of Git Flow and trunk-based development.

At all times, there will be two special, protected branches present:

- `develop`, a fairly stable branch, containing the latest merged changes from feature branches
- `main`, the stable production branch, containing cherry-picked commits from `develop`

The commit history of those branches must not be modified (i.e. no force pushing).

To develop new changes (features, fixes, etc.), one must create a short-lived feature branch from `develop`. The commit history may be freely modified, and the commits themselves need not be rigorously named nor structured.

Once the changes are ready to be incorporated, the developer must create a Pull Request (PR, also: Merge Request) onto `develop`.

Then, the proposed changes will be tested and reviewed by typically 1-2 other team-members, including those that depend on that feature for their own work.
Provided that the changes are approved, they will be merged into `develop` using the squash merge method, and the feature branch will be removed.

At the end of each cycle (or more frequently), the latest stable state of `develop` will be committed into `main` by cherry-picking the stable commits.

## Git naming conventions

As stated above, feature branch commits need not follow a naming standard.

However, the PR itself (and conversely the merge commit) must follow a variation of the [Conventional Commits specification](https://www.conventionalcommits.org/en/v1.0.0/).
The merge commit message should contain the id of the PR in parentheses.

The main highlighted commit types are as follows:

- `feat`: for features
- `fix`: for bug-fixes and other minor changes
- `chore`: for other, non-development related changes.

Note: the first word of the commit message description must be capitalized.

### Examples

- `feat: Database seeder`
- `fix(ui): Misplaced button`
- `refactor: Backend file structure (#12)`

---

Commits on the `main` branch must be in the form of `WW.MINOR` following [CalVer](https://calver.org/).

## Other conventions

Feature branches must be minimal, i.e. consist of changes relevant to just one task unit.

Apart from following the naming conventions, the PRs should:

- clearly state what changes are being introduced
- include ["Closes #1, #2, ..."](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically) in the description for any related issues that are resolved by that PR
- have their conflicts resolved by rebasing or manual resolution by the PR owner
- include tests
- pass the CI/CD pipeline
- be tested and reviewed by at least 1 other team-member

## Prerequisites

[Intellij IDE](https://www.jetbrains.com/idea/)

The usage of a Git GUI is encouraged.
