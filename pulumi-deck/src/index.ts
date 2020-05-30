import { IDeckPlugin } from '@spinnaker/core';
import { pulumiStage, initialize } from './PulumiStage';

export const plugin: IDeckPlugin = {
  initialize,
  stages: [pulumiStage],
};
