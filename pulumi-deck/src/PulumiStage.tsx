import React from 'react';

import {
  ExecutionDetailsSection,
  ExecutionDetailsTasks,
  FormikFormField,
  FormikStageConfig,
  FormValidator,
  HelpContentsRegistry,
  HelpField,
  IExecutionDetailsSectionProps,
  IStage,
  IStageConfigProps,
  IStageTypeConfig,
  NumberInput,
  TextInput,
  Validators,
} from '@spinnaker/core';

import './PulumiStage.less';

export function PulumiStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <div>
        <p>Pulumi execution complete!</p>
      </div>
    </ExecutionDetailsSection>
  );
}

/*
  IStageConfigProps defines properties passed to all Spinnaker Stages.
  See IStageConfigProps.ts (https://github.com/spinnaker/deck/blob/master/app/scripts/modules/core/src/pipeline/config/stages/common/IStageConfigProps.ts) for a complete list of properties.
  Pass a JSON object to the `updateStageField` method to add the `maxWaitTime` to the Stage.

  This method returns JSX (https://reactjs.org/docs/introducing-jsx.html) that gets displayed in the Spinnaker UI.
 */
function PulumiStageConfig(props: IStageConfigProps) {
  return (
    <div className="PulumiStageConfig">
      <FormikStageConfig
        {...props}
        validate={validate}
        onChange={props.updateStage}
        render={(props) => (
          <FormikFormField
            name="cloudProvider"
            label="Cloud Provider"
            input={(props) => <TextInput defaultValue="aws" {...props} />}
          />
        )}
      />
      <FormikStageConfig
        {...props}
        validate={validate}
        onChange={props.updateStage}
        render={(props) => (
          <FormikFormField
            name="account.accessToken"
            label="Pulumi Access Token"
            help={<HelpField id="armory.pulumiStage.account.accessToken" />}
            input={(props) => <TextInput {...props} />}
          />
        )}
      />
      <FormikStageConfig
        {...props}
        validate={validate}
        onChange={props.updateStage}
        render={(props) => (
          <FormikFormField
            name="account.stackName"
            label="Pulumi Stack Name"
            input={(props) => <TextInput {...props} />}
          />
        )}
      />
      <FormikStageConfig
        {...props}
        validate={validate}
        onChange={props.updateStage}
        render={(props) => (
          <FormikFormField
            name="creedentials.secretKeyId"
            label="AWS Access Key ID"
            input={(props) => <TextInput {...props} />}
          />
        )}
      />
      <FormikStageConfig
        {...props}
        validate={validate}
        onChange={props.updateStage}
        render={(props) => (
          <FormikFormField
            name="credentials.secretAccessKey"
            label="AWS Secret Access Key"
            input={(props) => <TextInput {...props} />}
          />
        )}
      />
      <FormikStageConfig
        {...props}
        validate={validate}
        onChange={props.updateStage}
        render={(props) => (
          <FormikFormField
            name="version"
            label="Pulumi CLI Version"
            input={(props) => <TextInput defaultValue="latest" {...props} />}
          />
        )}
      />
    </div>
  );
}

/*
  This is a contrived example of how to use an `initialize` function to hook into arbitrary Deck services. 
  This `initialize` function provides the help field text for the `RandomWaitStageConfig` stage form defined above.

  You can hook into any service exported by the `@spinnaker/core` NPM module, e.g.:
   - CloudProviderRegistry
   - DeploymentStrategyRegistry

  When you use a registry, you are diving into Deck's implementation to add functionality. 
  These registries and their methods may change without warning.
*/
export const initialize = () => {
  HelpContentsRegistry.register('armory.pulumiStage.account.accessToken', 'The Pulumi Access Token for the Pulumi CLI to use to login non-interactively.');
};

function validate(stageConfig: IStage) {
  const validator = new FormValidator(stageConfig);

  validator
    .field('accessToken')
    .required()
    .withValidators((value, label) => (value === '' ? `${label} must be non-empty` : undefined));

  return validator.validateForm();
}

export namespace PulumiStageExecutionDetails {
  export const title = 'pulumi';
}

/*
  Define Spinnaker Stages with IStageTypeConfig.
  Required options: https://github.com/spinnaker/deck/master/app/scripts/modules/core/src/domain/IStageTypeConfig.ts
  - label -> The name of the Stage
  - description -> Long form that describes what the Stage actually does
  - key -> A unique name for the Stage in the UI; ties to Orca backend
  - component -> The rendered React component
  - validateFn -> A validation function for the stage config form.
 */
export const pulumiStage: IStageTypeConfig = {
  key: 'pulumi',
  label: `Run Pulumi`,
  description: 'Stage that runs a Pulumi CLI command for a Pulumi app',
  component: PulumiStageConfig, // stage config
  executionDetailsSections: [PulumiStageExecutionDetails, ExecutionDetailsTasks],
  validateFn: validate,
};
