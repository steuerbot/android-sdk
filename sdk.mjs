import chalk from "chalk";
import yargs from "yargs";
import {hideBin} from "yargs/helpers";
import fs, {outputFile} from "fs-extra";
import {dirname} from 'path';
import {fileURLToPath} from 'url';
import {exec} from "child_process";
import inquirer from 'inquirer';

const __dirname = dirname(fileURLToPath(import.meta.url));

const packageJsonPath = `${__dirname}/package.json`;
let packageJsonContent = fs.readFileSync(packageJsonPath, 'utf-8');
const oldVersionName = packageJsonContent.match(
  /"version": "((\d+\.\d+\.\d+)(-.*)?)"/,
)[1];

const execAsync = (cmd) => {
  return new Promise((resolve, reject) => {
    const command = exec(`cd ${__dirname}; ${cmd}`);

    command.stdout.on('data', function (data) {
      console.log(data.toString());
    });

    command.stderr.on('data', function (data) {
      console.log(chalk.red(data.toString()));
    });

    command.on('error', (error) => {
      console.log(chalk.red(error.message));
      reject();
    });

    command.on('exit', function (code) {
      if(!code) {
        resolve();
      } else {
        reject();
      }
    });
  })
}

yargs(hideBin(process.argv))
  .command('change-version [push]', 'Set the new version', () => {}, async (argv) => {
    console.log(chalk.blue(`Current version is ${chalk.bold(oldVersionName)}`));

    let [v1, v2, v3] = oldVersionName
      .split('-')[0]
      .split('.')
      .map((str) => parseInt(str, 10));

    const majorVersion = `${v1 + 1}.0.0`;
    const minorVersion = `${v1}.${v2 + 1}.0`;
    const patchVersion = `${v1}.${v2}.${v3 + 1}`;
    const internalVersion = `${v1}.${v2}.${v3}`;

    const { suffix, suffixVersion, versionName } = await inquirer.prompt([
      {
        type: 'list',
        name: 'versionName',
        message: 'What version do you want to create?',
        default: 'internal',
        choices: [
          {
            name: `internal (${internalVersion})`,
            value: internalVersion,
            short: internalVersion,
          },
          {
            name: `patch    (${patchVersion})`,
            value: patchVersion,
            short: patchVersion,
          },
          {
            name: `minor    (${minorVersion})`,
            value: minorVersion,
            short: minorVersion,
          },
          {
            name: `major    (${majorVersion})`,
            value: majorVersion,
            short: majorVersion,
          },
          {
            name: `custom   (x.x.x)`,
            value: 'custom',
            short: 'custom',
          },
        ],
      },
      {
        type: 'input',
        name: 'versionName',
        when: ({ versionName }) => versionName === 'custom',
        message: 'What version string do you want to use?',
        askAnswered: true,
        validate: (input) =>
          !!input.match(/^\d+.\d+.\d+$/) ||
          'The version string must be in format *.*.*',
      },
      {
        type: 'list',
        name: 'suffix',
        message: 'What suffix do you want to use?',
        default: 'none',
        choices: [
          'none',
          {
            name: 'snapshot',
            value: 'SNAPSHOT',
            short: 'snapshot',
          },
          'alpha',
          'beta',
          'rc',
        ],
      },
      {
        type: 'number',
        name: 'suffixVersion',
        when: ({ suffix }) => suffix === 'rc',
        message: 'What RC do you want to create?',
        default: '1',
        transformer: (input) => `rc.${input}`,
      },
    ]);

    let addon =
      suffix === 'none'
        ? ''
        : `-${suffix}${suffixVersion ? `.${suffixVersion}` : ''}`;
    const newVersion = `${versionName}${addon}`;
    const changeFile = async (files, search, replace) => {
      for (const file of files) {
        const path = `${__dirname}/${file}`;
        const content = await fs.readFile(path, 'utf-8');
        await outputFile(path, content.replace(search, replace, 'utf-8'));
      }
    }
    await changeFile(
      [
        'package.json',
      ],
      /"version": "[^"]*",/,
      `"version": "${newVersion}",`
    );
    await changeFile(
      [
        'README.md',
        'examples/basic/app/build.gradle',
        'examples/dynamic/app/build.gradle',
        'examples/fragment/app/build.gradle',
        'examples/inheritance/app/build.gradle',
      ],
      /implementation 'com.steuerbot.sdk:sdk:[^']*'/,
      `implementation 'com.steuerbot.sdk:sdk:${newVersion}'`
    );

    if(argv.push) {
      const branchName = `v${newVersion}`;
      await execAsync(`git fetch`);
      await execAsync(`git pull`);
      await execAsync(`git checkout -b ${branchName}`);
      await execAsync('git add -A');
      try {
        await execAsync(`git commit -m "Create release ${newVersion}"`);
      } catch(e) {
        // no changes detected
      }
      await execAsync(`git push origin ${branchName}`);
    }

    console.log(chalk.green(`✓ Set version to ${newVersion}`));
  })
  .parse();
