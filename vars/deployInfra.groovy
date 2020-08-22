#!/usr/bin/env groovy

def call() {
  dir("${self_repo_tf_src}") {

    withCredentials([
      string(credentialsId: 'NONPROD_AZURE_CLIENT_ID', variable: 'NONPROD_AZURE_CLIENT_ID'),
      string(credentialsId: 'NONPROD_AZURE_CLIENT_SECRET', variable: 'NONPROD_AZURE_CLIENT_SECRET'),
      string(credentialsId: 'NONPROD_AZURE_TENANT_ID', variable: 'NONPROD_AZURE_TENANT_ID'),
      string(credentialsId: 'NONPROD_AZURE_SUBSCRIPTION_ID', variable: 'NONPROD_AZURE_SUBSCRIPTION_ID')
    ]) {
      sh(
        script: """#!/bin/bash
          bash "${dynamic_build_scripts_directory}/util-azure-login.bash" \
            -a "${NONPROD_AZURE_CLIENT_ID}" \
            -b "${NONPROD_AZURE_CLIENT_SECRET}" \
            -c "${NONPROD_AZURE_TENANT_ID}" \
            -d "${NONPROD_AZURE_SUBSCRIPTION_ID}"
        """,
        label: "Login: Azure CLI"
      )
    }

    sh(
      script: """#!/bin/bash
        bash "${dynamic_build_scripts_directory}/util-azure-register-pod-identity.bash"
      """,
      label: "Register Pod Identity Previews"
    )

    withCredentials([
      string(credentialsId: 'NONPROD_AZURE_CLIENT_ID', variable: 'NONPROD_TERRAFORM_BACKEND_AZURE_CLIENT_ID'),
      string(credentialsId: 'NONPROD_AZURE_CLIENT_SECRET', variable: 'NONPROD_TERRAFORM_BACKEND_AZURE_CLIENT_SECRET'),
      string(credentialsId: 'NONPROD_AZURE_TENANT_ID', variable: 'NONPROD_TERRAFORM_BACKEND_AZURE_TENANT_ID'),
      string(credentialsId: 'NONPROD_AZURE_SUBSCRIPTION_ID', variable: 'NONPROD_TERRAFORM_BACKEND_AZURE_SUBSCRIPTION_ID')
    ]) {
      sh(
        script: """#!/bin/bash
          bash "${dynamic_build_scripts_directory}/deploy-azure-terraform-init.bash" \
            -a "${NONPROD_TERRAFORM_BACKEND_AZURE_CLIENT_ID}" \
            -b "${NONPROD_TERRAFORM_BACKEND_AZURE_CLIENT_SECRET}" \
            -c "${NONPROD_TERRAFORM_BACKEND_AZURE_TENANT_ID}" \
            -d "${NONPROD_TERRAFORM_BACKEND_AZURE_SUBSCRIPTION_ID}" \
            -e "${terraform_state_rg}" \
            -f "${terraform_state_storage}" \
            -g "${terraform_state_container}" \
            -h "${terraform_state_key}" \
            -i "${terraform_state_workspace}"
        """,
        label: "Terraform: Initialise and Set Workspace"
      )
    }

    withCredentials([
      string(credentialsId: 'NONPROD_AZURE_CLIENT_ID', variable: 'NONPROD_AZURE_CLIENT_ID'),
      string(credentialsId: 'NONPROD_AZURE_CLIENT_SECRET', variable: 'NONPROD_AZURE_CLIENT_SECRET'),
      string(credentialsId: 'NONPROD_AZURE_TENANT_ID', variable: 'NONPROD_AZURE_TENANT_ID'),
      string(credentialsId: 'NONPROD_AZURE_SUBSCRIPTION_ID', variable: 'NONPROD_AZURE_SUBSCRIPTION_ID')
    ]) {
      sh(
        script: """#!/bin/bash
          bash "${dynamic_build_scripts_directory}/deploy-azure-terraform-plan.bash" \
            -a "${NONPROD_AZURE_CLIENT_ID}" \
            -b "${NONPROD_AZURE_CLIENT_SECRET}" \
            -c "${NONPROD_AZURE_TENANT_ID}" \
            -d "${NONPROD_AZURE_SUBSCRIPTION_ID}"
        """,
        label: "Terraform: Plan"
      )

      sh(
        script: """#!/bin/bash
          bash "${dynamic_build_scripts_directory}/deploy-azure-terraform-apply.bash" \
            -a "${NONPROD_AZURE_CLIENT_ID}" \
            -b "${NONPROD_AZURE_CLIENT_SECRET}" \
            -c "${NONPROD_AZURE_TENANT_ID}" \
            -d "${NONPROD_AZURE_SUBSCRIPTION_ID}"
        """,
        label: "Terraform: Apply"
      )
    }

    // Extract needed Terraform Outputs into Jenkins Variables
    // These are used in the next stage
    script {
      env.dynamic_cosmosdb_endpoint = sh(
        script:"""#!/bin/bash
          terraform output cosmosdb_endpoint
        """,
        returnStdout: true,
        label: "Terraform: Extract ComsosDB Endpoint URL"
      )

      env.dynamic_cosmosdb_primary_master_key = sh(
        script:"""#!/bin/bash
          terraform output cosmosdb_primary_master_key
        """,
        returnStdout: true,
        label: "Terraform: Extract ComsosDB Primary Master Key"
      )

      env.dynamic_app_insights_instrumentation_key = sh(
        script:"""#!/bin/bash
          terraform output app_insights_instrumentation_key
        """,
        returnStdout: true,
        label: "Terraform: Extract Application Insights Key"
      )
    }

  }
}
