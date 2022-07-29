import chalk from "chalk";
import yargs from "yargs";
import {hideBin} from "yargs/helpers";
import fs, {outputFile} from "fs-extra";
import {dirname} from 'path';
import {fileURLToPath} from 'url';

const __dirname = dirname(fileURLToPath(import.meta.url));

yargs(hideBin(process.argv))
  .command('set-version <newVersion>', 'Set the new verison', () => {}, async (argv) => {
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
        'examples/dynamic/steuerbot/build.gradle',
        'examples/fragment/app/build.gradle',
        'examples/inheritance/app/build.gradle',
      ],
      /implementation 'com.steuerbot.sdk:sdk:[^']*'/,
      `implementation 'com.steuerbot.sdk:sdk:${argv.newVersion}'`
    );

    console.log(chalk.green(`✓ Set version to ${argv.newVersion}`));
  })
  .parse();
