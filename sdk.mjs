import chalk from "chalk";
import yargs from "yargs";
import {hideBin} from "yargs/helpers";
import fs, {outputFile} from "fs-extra";
import {dirname} from 'path';
import {fileURLToPath} from 'url';
import {exec} from "child_process";

const __dirname = dirname(fileURLToPath(import.meta.url));

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
  .command('set-version <newVersion> [push]', 'Set the new verison', () => {}, async (argv) => {
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
      `"version": "${argv.newVersion}",`
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
      `implementation 'com.steuerbot.sdk:sdk:${argv.newVersion}'`
    );

    if(argv.push) {
      const branchName = `v${argv.newVersion}`;
      await execAsync(`git fetch`);
      await execAsync(`git pull`);
      await execAsync(`git checkout -b ${branchName}`);
      await execAsync('git add -A');
      try {
        await execAsync(`git commit -m "Create release ${argv.newVersion}"`);
      } catch(e) {
        // no changes detected
      }
      await execAsync(`git push origin ${branchName}`);
    }

    console.log(chalk.green(`✓ Set version to ${argv.newVersion}`));
  })
  .parse();
