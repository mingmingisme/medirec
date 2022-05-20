from utils import *
import torch
import numpy as np
import cv2
import pydicom
import matplotlib.pyplot as plt
from skimage import img_as_float32 as img_as_float
import sys
import os


def jpg_to_net(inputUrl, outputUrl, model_param):
    input_img = cv2.imread(inputUrl, 0)
    input_img = img_as_float(input_img)
    input_img = torch.Tensor(input_img).unsqueeze(0).unsqueeze(0).to('cuda:0')
    # model_param = './result_model/FbpConvNet_norm.pkl'
    net = FBPCONVNet().to('cuda:0')
    net.load_state_dict(torch.load(model_param), strict=True)
    net.eval()
    with torch.no_grad():
        output = net(input_img)
    output = output.to('cpu').squeeze(0).squeeze(0).numpy()
    plt.imsave(outputUrl, output, cmap='gray')


if __name__ == '__main__':
    print(f'Current path is {os.getcwd()}')
    inputUrl = sys.argv[1]
    outputUrl = sys.argv[2]
    model_param = sys.argv[3]
    print('Converting image from {} to {}...'.format(inputUrl, outputUrl))
    jpg_to_net(inputUrl, outputUrl, model_param)
    print("Finish conversion")
