# Link-GPT

[![Rust CI](https://github.com/iashchak/audiencer/actions/workflows/main.yml/badge.svg?branch=main)](https://github.com/iashchak/audiencer/actions/workflows/main.yml)

This is a rust library for generating text using [🤗 Link-GPT](https://huggingface.co/iashchak/link-gpt-7b)

Link-GPT is a Mistral-7B model fine-tuned on a large dataset from telegram conversations of [▶️ Igor Link](https://www.youtube.com/@Igor_Link) fan-club.

## Installation

### Ready to use

Take the latest version of built artifact from releases (TBD)

### Build from source

1. Install [Rust](https://www.rust-lang.org/tools/install)
2. Clone [the repo](https://github.com/iashchak/audiencer.git)

    ```sh
    git clone https://github.com/iashchak/audiencer.git
    ```
3. Build

    ```sh
    cd audiencer
    cargo run --release link_gpt_app
    ```

## JNI bindings

TBD

